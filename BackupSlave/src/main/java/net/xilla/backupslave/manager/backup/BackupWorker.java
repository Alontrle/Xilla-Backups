package net.xilla.backupslave.manager.backup;

import com.mongodb.client.MongoCollection;
import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import net.xilla.backupcore.api.database.MongoDocument;
import net.xilla.backupcore.api.database.worker.client.ClientManager;
import net.xilla.backupcore.api.worker.Worker;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.filesystem.filemap.FileMapObject;
import net.xilla.backupcore.nodesystem.clientserver.ClientServerObject;
import net.xilla.backupslave.Main;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackupWorker extends Worker {

    private ArrayList<Long> backupTimes;
    private ZoneId zoneID;
    private long lastBackup;
    private long currentBackup;
    private Config settings;
    private Path directory;

    private HashMap<String, ClientServerObject> clientServers; // Key : Client Server ID, Value : Client Server Object

    public BackupWorker() {
        super(60000);
        this.settings = ConfigManager.getInstance().getConfig("settings.json");
        this.zoneID = ZoneId.of(settings.getString("timezone"));
        this.lastBackup = settings.getInt("last-backup");
        this.directory = Paths.get(settings.getString("backup-directory"));
    }

    public Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneID).toInstant());
    }

    public void loadBackupTimes() {
        Date start = atStartOfDay(new Date());
        long durationInMs = start.getTime();

        backupTimes = new ArrayList<>();
        for(String time : settings.getStringList("backup-times")) {
            try {
                String[] timeSplit = time.split(":");
                long hours = Long.parseLong(timeSplit[0]) * 1000 * 60 * 60;
                long minutes = Long.parseLong(timeSplit[1]) * 1000 * 60;

                long backupTime = durationInMs + hours + minutes;
                if (System.currentTimeMillis() <= backupTime) {
                    backupTimes.add(backupTime);
                }

            } catch(Exception e) {
                Log.sendMessage(2, "Invalid backup time!");
            }
        }
    }

    @Override
    public Boolean runWorker(long start) {
        if(backupTimes == null || backupTimes.size() == 0) {
            loadBackupTimes();
        }
        if(backupTimes.size() > 0) {
            long currentBackup = backupTimes.get(0);
            if (lastBackup < currentBackup && currentBackup <= System.currentTimeMillis()) {
                runBackup();
                this.lastBackup = currentBackup;
                this.currentBackup = currentBackup;
                settings.set("last-backup", currentBackup);
                settings.save();
                backupTimes.remove(0);
            }
        }
        return true;
    }

    public void runBackup() {
        this.clientServers = new HashMap<>();

        try {
            Stream<Path> paths = Files.walk(directory);
            paths.forEach(this::queueServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(ClientServerObject clientServerObject : clientServers.values()) {
            MongoDocument mongoDocument = new MongoDocument(clientServerObject.toDocument(), BackupCore.getInstance().getNodeID(), "Master");
            BackupCore.getInstance().getMongoManager().getClientWorker().addDocument(mongoDocument);
        }
    }

    private void queueServer(Path path) {
        if(!path.toString().equalsIgnoreCase(directory.toString()) && !path.toFile().isDirectory()) {
            String serverID = path.toString().replace(settings.getString("backup-directory"), "").split("\\\\")[0] + "-pending";
            if(!clientServers.containsKey(serverID)) {
                ClientServerObject client = new ClientServerObject(serverID);
                clientServers.put(serverID, client);
            }
            try {
                InputStream is = Files.newInputStream(path);
                String hash = DigestUtils.md5Hex(is);
                FileObject fileObject = new FileObject(serverID + "-" + hash, path.toString(), "none", "none", path.toFile().lastModified());

                clientServers.get(serverID).addFile(fileObject, currentBackup);

            } catch (IOException e) {
                Log.sendMessage(2, "Failed to load file " + path);
            }
        }
    }

    public void forceBackup() {
        runBackup();
        this.lastBackup = currentBackup;
        this.currentBackup = System.currentTimeMillis();
        settings.set("last-backup", currentBackup);
        settings.save();
    }
}
