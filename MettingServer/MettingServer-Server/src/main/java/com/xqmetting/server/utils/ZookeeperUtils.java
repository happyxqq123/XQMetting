package com.xqmetting.server.utils;

public class ZookeeperUtils {

    public static final String MANAGE_PATH ="/mt/nodes";
    public static final String PATH_PREFIX = MANAGE_PATH + "/seq-";


    public static long getIdByPath(String path,String prefix) {
        String sid = null;
        if (null == path) {
            throw new RuntimeException("节点路径有误");
        }
        int index = path.lastIndexOf(prefix);
        if (index >= 0) {
            index += prefix.length();
            sid = index <= path.length() ? path.substring(index) : null;
        }

        if (null == sid) {
            throw new RuntimeException("节点ID获取失败");
        }
        return Long.parseLong(sid);
    }
}
