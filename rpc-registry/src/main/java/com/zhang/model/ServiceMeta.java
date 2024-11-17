package com.zhang.model;

/**
 * @ClassName ServiceMeta
 * @Description 服务元数据信息，存储在注册中心
 * @Author zhanggen
 * @Date 2024/10/31 16:48
 * @Version 1.0
 */
public class ServiceMeta {
    private String Name;

    private String Version;

    private String Addr;

    private int Port;

    public ServiceMeta() {
    }

    public ServiceMeta(String name, String version, String addr, int port) {
        Name = name;
        Version = version;
        Addr = addr;
        Port = port;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    @Override
    public String toString() {
        return "ServiceMeta{" +
                "Name='" + Name + '\'' +
                ", Version='" + Version + '\'' +
                ", Addr='" + Addr + '\'' +
                ", Port=" + Port +
                '}';
    }
}
