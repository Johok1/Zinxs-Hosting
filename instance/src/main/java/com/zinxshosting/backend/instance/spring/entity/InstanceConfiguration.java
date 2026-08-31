package com.zinxshosting.backend.instance.spring.entity;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zinxshosting.backend.instance.config.TransactionConfiguration;
import com.zinxshosting.backend.instance.controller.loadbalancer.traits.Trait;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Getter
@Setter
@Entity
public class InstanceConfiguration {

    @SequenceGenerator(
            name = "instance_sequence",
            sequenceName = "instance_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "instance_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String hostname;

    @Column(nullable = false)
    private Integer port;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private List<Trait> traits;

    @Column(nullable = false)
    private TransactionConfiguration tconfig;

    public InstanceConfiguration(String username, String hostname, Integer port, String password, List<Trait> traits, TransactionConfiguration tconfig){
        this.username = username;
        this.hostname = hostname;
        this.port = port;
        this.password = password;
        this.traits = traits;
        this.tconfig = tconfig;
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
