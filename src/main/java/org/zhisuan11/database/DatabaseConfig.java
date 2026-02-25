package org.zhisuan11.database;

// 数据库设置
public class DatabaseConfig {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private boolean useSSL;
    private String type;

    // 默认构造函数
    public DatabaseConfig() {
        this.type = "mysql";
        this.host = "localhost";
        this.port = 3306;
        this.database = "zhisuan11";
        this.username = "root";
        this.password = "";
        this.useSSL = false;
    }

    // 接口
    // public String getHost() {return host;}
    public void setHost(String host) {this.host = host;}

    // public int getPort() {return port;}
    public void setPort(int port) {this.port = port;}

    // public String getDatabase() {return database;}
    public void setDatabase(String database) {this.database = database;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    // public boolean isUseSSL() {return useSSL;}
    public void setUseSSL(boolean ssl) {this.useSSL = ssl;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getJdbcUrl() {
        if (type.equalsIgnoreCase("mysql")) {
            return String.format("jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=UTC",
                    host, port, database, useSSL);
        } else {
            return "jdbc:sqlite:plugins/QuizPlugin/quiz.db";
        }
    }
}
