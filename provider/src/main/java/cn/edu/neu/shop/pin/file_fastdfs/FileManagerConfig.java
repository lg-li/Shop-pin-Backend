package cn.edu.neu.shop.pin.file_fastdfs;

import java.io.Serializable;

public interface FileManagerConfig extends Serializable {

    public static final String FILE_DEFAULT_AUTHOR = "Pin";

    public static final String PROTOCOL = "http://";

    public static final String SEPARATOR = "/";

    public static final String TRACKER_NGNIX_ADDR = "47.93.218.67";

    public static final String TRACKER_NGNIX_PORT = ":8888";

    public static final String CLIENT_CONFIG_FILE = "/usr/java/pin/fdfs_client.conf";
}
