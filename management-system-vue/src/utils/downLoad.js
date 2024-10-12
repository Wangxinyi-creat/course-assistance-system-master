import {Message} from "element-ui";
import request from '@/utils/request';

/**
 * EXCEl通用下载工具  调用接口后在调用
 * 注：接口请求需添加：responseType: 'blob'
 *
 * @param res   导出接口返回值
 * @returns {Promise<AxiosResponse<any>>}
 */
export function download(res) {
    const content = res.data
    // let filename = res.headers["content-disposition"].split(';')[1];
    const headerFilename = res.headers['content-disposition']?.split(';')[1].split('=')[1];
    const filename = decodeURIComponent(headerFilename);
    const blob = new Blob([content], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"})
    if ('download' in document.createElement('a')) {
        const elink = document.createElement('a')
        elink.download = filename
        elink.style.display = 'none'
        elink.href = URL.createObjectURL(blob)
        document.body.appendChild(elink)
        elink.click()
        URL.revokeObjectURL(elink.href)
        document.body.removeChild(elink)
        Message.success("导出成功")
    } else {
        navigator.msSaveBlob(blob, filename)
    }
}


/**
 * EXCEl通用下载工具  url+data
 *
 * @param url  地址
 * @param data 查询参数
 * @returns {Promise<AxiosResponse<any>>}
 */
export function downloadByUrl(url, data) {
    return request.post(url, data, {responseType: 'blob'}).then((res) => {
        download(res)
    }).catch((r) => {
        Message.error("发生错误:" + r)
        console.error(r)
    })
}
