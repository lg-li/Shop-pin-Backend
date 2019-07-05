package cn.edu.neu.shop.pin.file_fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.File;

/**
 * 概要： FastDFS Java客户端工具类
 *
 * @author Niucheng
 */
public class FileManager implements FileManagerConfig {

    private static final long serialVersionUID = 1L;
    private static ThreadLocal<TrackerClient> trackerClient = new ThreadLocal<>();
    private static ThreadLocal<TrackerServer> trackerServer = new ThreadLocal<>();
    private static ThreadLocal<StorageServer> storageServer = new ThreadLocal<>();
    private static ThreadLocal<StorageClient> storageClient = new ThreadLocal<>();

    /**
     * 方法概要： 文件上传
     * @return fileAbsolutePath
     *
     */
    public static String upload(FastDFSFile file,NameValuePair[] valuePairs) {
        try {
            String classPath = new File(FileManager.class.getResource("/").getFile()).getCanonicalPath();

            String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;
            ClientGlobal.init(fdfsClientConfigFilePath);

            trackerClient.set(new TrackerClient());
            trackerServer.set(trackerClient.get().getConnection());

            storageClient.set(new StorageClient(trackerServer.get(), storageServer.get()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] uploadResults = null;
        try {
            uploadResults = storageClient.get().upload_file(file.getContent(),file.getExt(), valuePairs);
            String groupName = uploadResults[0];
            String remoteFileName = uploadResults[1];
            return PROTOCOL
                    + TRACKER_NGNIX_ADDR
                    + TRACKER_NGNIX_PORT
                    + SEPARATOR + groupName
                    + SEPARATOR + remoteFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}