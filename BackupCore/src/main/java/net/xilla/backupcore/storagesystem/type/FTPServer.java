package net.xilla.backupcore.storagesystem.type;

import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.storagesystem.StorageRequest;
import net.xilla.backupcore.storagesystem.StorageServerError;
import net.xilla.backupcore.storagesystem.StorageServerObject;
import org.apache.commons.net.ftp.FTPClient;
import org.bson.Document;

import java.io.*;
import java.util.List;

public class FTPServer extends StorageServerObject {

    private FTPClient client;

    public FTPServer(String id, String host, int port, String user, String password, List<String> usedIDs) {
        super(id, host, port, user, password, usedIDs, "FTP");
        this.client = new FTPClient();
        client.setDefaultPort(getPort());

        open();
    }

    public FTPServer(Document document) {
        super(document);
        this.client = new FTPClient();
        client.setDefaultPort(getPort());

        open();
    }

    @Override
    public boolean processItem(Object object) {
        StorageRequest storageRequest = (StorageRequest)object;
        if(storageRequest.getType().equalsIgnoreCase("Upload")) {
            uploadFile(storageRequest.getFileObject());
        } else if(storageRequest.getType().equalsIgnoreCase("Download")) {
            downloadFile(storageRequest.getFileObject());
        } else if(storageRequest.getType().equalsIgnoreCase("Delete")) {
            deleteFile(storageRequest.getFileObject());
        }
        return true;
    }

    @Override
    public boolean open() {
        try {
            client.connect(getHost(), getPort());
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            client.login(getUser(), getPassword());
        } catch (IOException ex) {
            client = null;
            new StorageServerError().reportError("Failed to connect to FTP server. " + getID());
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean close() {
        try {
            client.logout();
        } catch (IOException ex) {
            new StorageServerError().reportError("Failed to disconnect to FTP server. " + getID());
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean uploadFile(FileObject file) {
        if(client != null) {
            InputStream is = null;
            try {
                is = new FileInputStream(file.getLocalPath());
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                client.storeFile(file.getRemotePath() + ".backup", is);
                try {
                    is.close();
                } catch (IOException ignored) { }
                return true;
            } catch (FileNotFoundException ex) {
                new StorageServerError().reportError("File not found! (" + file.getLocalPath() + ")");
                try {
                    assert is != null;
                    is.close();
                } catch (Exception ignored) {
                }

                return false;
            } catch (IOException ex) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }

                new StorageServerError().reportError("Failed to upload file. (" + file.getLocalPath() + ")");
                //ex.printStackTrace();
                return false;
            }
        } else {
            new StorageServerError().reportError("The FTP Server could not connect!");
            return false;
        }
    }

    @Override
    public boolean downloadFile(FileObject file) {
        if(client != null) {
            FileOutputStream os = null;
            try {
                new File(file.getLocalPath()).getParentFile().mkdirs();
                new File(file.getLocalPath()).createNewFile();
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                os = new FileOutputStream(file.getLocalPath());
                client.retrieveFile(file.getRemotePath() + ".backup", os);
                try {
                    os.close();
                } catch (IOException ignored) { }
                return true;
            } catch (FileNotFoundException ex) {
                new StorageServerError().reportError("File not found! (" + file.getLocalPath() + ")");
                ex.printStackTrace();

                return false;
            } catch (IOException ex) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }

                new StorageServerError().reportError("Failed to download file. (" + file.getLocalPath() + ")");
                //ex.printStackTrace();
                return false;
            }
        } else {
            new StorageServerError().reportError("The FTP Server could not connect!");
            return false;
        }
    }

    @Override
    public boolean deleteFile(FileObject file) {
        if(client != null) {
            try {
                client.deleteFile(file.getRemotePath() + ".file");
                return true;
            } catch (IOException ex) {
                new StorageServerError().reportError("Failed to delete file. (" + file.getRemotePath() + ")");
                return false;
            }
        } else {
            Log.sendMessage(2, "The FTP Server could not connect!");
            return false;
        }
    }

    @Override
    public Document toDocument() {
        return super.toDocument().append("type", "ftp");
    }

}
