import axios from 'axios'


const request = axios.create({

    baseURL: import.meta.env.VITE_APP_BASE_API + "/server/admin-api", // 此处的 /admin-api/ 地址，原因是后端的基础路径为 /admin-api/
    // 指定请求超时的毫秒数(0 表示无超时时间)
    timeout: 30000,
    // 禁用 Cookie 等信息
    withCredentials: false,
});


/*导出异步请求 */
export default request;
