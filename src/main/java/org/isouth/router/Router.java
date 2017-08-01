package org.isouth.router;

import java.util.Arrays;
import java.util.List;

/**
 * 路由器
 * Created by qiyi on 7/10/2017.
 */
public interface Router<H> {
    String GET_METHOD = "GET";
    String POST_METHOD = "POST";
    String PUT_METHOD = "PUT";
    String DELETE_METHOD = "DELETE";
    String HEAD_METHOD = "HEAD";
    String PATCH_METHOD = "PATCH";
    String OPTIONS_METHOD = "OPTIONS";
    String TRACE_METHOD = "TRACE";
    List<String> ALL_METHODS = Arrays.asList(
            GET_METHOD,
            POST_METHOD,
            PUT_METHOD,
            DELETE_METHOD,
            HEAD_METHOD,
            PATCH_METHOD,
            OPTIONS_METHOD,
            TRACE_METHOD
    );

    /**
     * 创建子应用
     *
     * @param relativePath 子应用相对路径
     * @return 子应用路由器
     */
    Router app(String relativePath);

    /**
     * 添加 HTTP 请求路由
     *
     * @param method HTTP 方法
     * @param path   HTTP 路径
     * @param h      HTTP 处理器
     * @return 路由名称
     */
    String route(String method, String path, H h);

    /**
     * 添加 GET 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String get(String path, H h) {
        return route(GET_METHOD, path, h);
    }

    /**
     * 添加 POST 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String post(String path, H h) {
        return route(POST_METHOD, path, h);
    }


    /**
     * 添加 PUT 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String put(String path, H h) {
        return route(PUT_METHOD, path, h);
    }

    /**
     * 添加 DELETE 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String delete(String path, H h) {
        return route(DELETE_METHOD, path, h);
    }


    /**
     * 添加 HEAD 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String head(String path, H h) {
        return route(HEAD_METHOD, path, h);
    }

    /**
     * 添加 OPTIONS 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String options(String path, H h) {
        return route(OPTIONS_METHOD, path, h);
    }


    /**
     * 添加 PATCH 请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default String patch(String path, H h) {
        return route(PATCH_METHOD, path, h);
    }

    /**
     * 添加所有方法请求路由
     *
     * @param path HTTP 路径
     * @param h    HTTP 处理器
     * @return 路由名称
     */
    default void any(String path, H h) {
        ALL_METHODS.forEach(method -> route(method, path, h));
    }

    /**
     * 移除路由
     *
     * @param name 路由名称
     */
    Router remove(String name);

    /**
     * 构建所有路由
     *
     * @return 路由器
     */
    Router build();

    /**
     * 匹配路由
     *
     * @param method HTTP 方法
     * @param path   HTTP 路径
     * @return 匹配到的 HTTP 处理器
     */
    H match(String method, String path);
}
