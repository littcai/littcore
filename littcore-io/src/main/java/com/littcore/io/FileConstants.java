package com.littcore.io;

/**
 * 
 * <pre>
 *  Title: 文件上传常量
 *  Description: 文件上传常量
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-4-27
 * @version 1.0
 */
public class FileConstants 
{
	/**
     * The number of bytes in a kilobyte.
     */
    public static final int ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final int ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final int ONE_GB = ONE_KB * ONE_MB;
	
	//上传Session最大生存期：十分钟
    public static final long TIME_TO_LIVE_MAX_MILLISECOND=10*60*1000;
    //上传Session ID长度
    public static final int SESSION_ID_SIZE=20;
    //上传Session管理器的时钟周期
    public final long TIMER_INTERVAL_MILLIS = 60000;
    //上传目录
    public static final String UPLOAD_DIR="/upload";
    //上传失败返回页面
    public static final String DEFAULT_UPLOAD_FAILURE_URL="./result.html";
    //用于在Session存储属性上传会话id的关键字
    public static final String SESSION_KEY="sessionId";
    //用于在Session存储属性转发URL的关键字
    public static final String FORWARDURL_KEY="forwardURL";
    //上传Session中标识文件上传状态Bean的关键字
    public static final String FILE_UPLOADSTATUS_BEAN_KEY="fileUploadStatusBean";
    //上传Session中标识文件上传状态BeanList的关键字
    public static final String FILE_UPLOADSTATUS_BEAN_LIST_KEY="fileUploadStatusBeanList";
    //设置内存阀值，超过后写入临时文件:5MB
    public static final int UPLOAD_SIZE_THRESHOLD=ONE_KB*ONE_KB*5;
    //单次最大上传量:100MB
    public static final int MAX_ONCE_UPLOAD_SIZE=ONE_KB*ONE_KB*100;
    //单次每文件最大上传量:100MB
    public static final int MAX_EACH_FILE_UPLOAD_SIZE=ONE_KB*ONE_KB*100;
    //是否扫描文件
    public static final boolean IS_SCAN_VIRUS=true;
}
