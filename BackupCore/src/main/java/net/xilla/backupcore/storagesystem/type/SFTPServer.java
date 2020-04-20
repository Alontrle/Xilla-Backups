package net.xilla.backupcore.storagesystem.type;

import com.jcraft.jsch.*;
import net.xilla.backupcore.api.Log;
import net.xilla.backupcore.filesystem.file.FileObject;
import net.xilla.backupcore.storagesystem.StorageRequest;
import net.xilla.backupcore.storagesystem.StorageServerError;
import net.xilla.backupcore.storagesystem.StorageServerObject;
import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SFTPServer extends StorageServerObject {

    private JSch jsch;
    private Session session;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;

    public SFTPServer(String id, String host, int port, String user, String password, List<String> usedIDs) {
        super(id, host, port, user, password, usedIDs, "SFTP");

        jsch = new JSch();
        try {
            session = jsch.getSession(getUser(), getHost(), getPort());
            session.setPassword(getPassword());
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException ex) {
            jsch = null;
            session = null;
            Log.sendMessage(2, "Failed to connect to SFTP server.");
            ex.printStackTrace();
        }
    }

    public SFTPServer(Document document) {
        super(document);
        jsch = new JSch();
        try {
            session = jsch.getSession(getUser(), getHost(), getPort());
            session.setPassword(getPassword());
            Log.sendMessage(0, document.toString());
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException ex) {
            jsch = null;
            session = null;
            Log.sendMessage(2, "Failed to connect to SFTP server.");
            ex.printStackTrace();
        }
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
        return true;
    }

    public boolean close() {
        this.session.disconnect();
        return true;
    }

    @Override
    public boolean uploadFile(FileObject fileObject) {
        if(jsch != null && session != null) {
            try {
                if(!session.isConnected())
                    session.connect();
                if(channel == null || !channel.isConnected()) {
                    channel = session.openChannel("sftp");
                    channel.connect();
                }
                channelSftp = (ChannelSftp) channel;
                File f = new File(fileObject.getLocalPath());
                channelSftp.put(new FileInputStream(f), fileObject.getRemotePath() + ".backup");
            } catch (JSchException | SftpException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            Log.sendMessage(2, "The SFTP Server could not connect!");
            return false;
        }
        return  false;
    }

    @Override
    public boolean downloadFile(FileObject fileObject) {
        if(jsch != null && session != null) {
            try {
                if(!session.isConnected())
                    session.connect();
                if(channel == null || !channel.isConnected()) {
                    channel = session.openChannel("sftp");
                    channel.connect();
                }
                channelSftp = (ChannelSftp) channel;
                channelSftp.get(fileObject.getRemotePath() + ".backup", fileObject.getLocalPath());
            } catch (JSchException | SftpException ex) {
                ex.printStackTrace();
            }
        } else {
            Log.sendMessage(2, "The SFTP Server could not connect!");
            return false;
        }
        return  false;
    }

    @Override
    public boolean deleteFile(FileObject fileObject) {
        if(jsch != null && session != null) {
            Channel channel = null;
            ChannelSftp channelSftp = null;
            try {
                channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp) channel;
                channelSftp.rm(fileObject.getRemotePath());
            } catch (JSchException | SftpException ex) {
                ex.printStackTrace();
            }
            if(channel != null)
                channel.disconnect();
            if(channelSftp != null && !channelSftp.isClosed())
                channelSftp.disconnect();
        } else {
            Log.sendMessage(2, "The SFTP Server could not connect!");
            return false;
        }
        return  false;
    }

    @Override
    public Document toDocument() {
        return super.toDocument().append("type", "sftp");
    }
}
