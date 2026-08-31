package com.zinxshosting.backend.instance.config;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

public class TestSSHConnection {


    String username, hostname, password;
    int port;
    @Test
    public void testSSH() throws JSchException, IOException {
        username = "ubuntu";
        hostname = "15.204.204.53";
        password = "sfJEUswZYKPp";

        port = 22;
        Session session = new JSch().getSession(username,hostname,port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking","no");
        session.connect();
        OutputStream out = new ByteArrayOutputStream();
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand("help");
        channelExec.setOutputStream(out);
        channelExec.connect();
        System.out.println(channelExec.isConnected());
        Scanner scanner = new Scanner(channelExec.getInputStream());
        System.out.println(channelExec.isConnected());
        while(scanner.hasNext()){
            System.out.println(scanner.nextLine());
        }
        System.out.println(session.isConnected());
    }

    /**
     * @return com.jcraft.jsch.Session
     * @throws JSchException
     */
    public Session configureSession() throws JSchException {
        Session session = new JSch().getSession(username, hostname, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }

    /**
     *
     * @return com.jcraft.jsch.Session
     * @throws JSchException
     */
    public Session connectSession() throws JSchException {
        Session session = configureSession();
        session.connect();
        return session;
    }

    /**
     *
     * @param session
     * @return
     * @throws JSchException
     */
    public ChannelExec openExecutionChannel(Session session) throws JSchException {
        return (ChannelExec) session.openChannel("exec");
    }

    /**
     *
     * @param command
     * @param channel
     * @return
     * @throws JSchException
     */
    public ByteArrayOutputStream executeCommand(String command, ChannelExec channel) throws JSchException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        channel.setCommand(command);
        channel.setOutputStream(outputStream);
        channel.connect();
        return outputStream;
    }

}
