// 导出参数
import request from "@/utils/request"

export async function exportHtml() {
    return request({
        url: '/infra/db-doc/export-html',
        method: 'get',
        responseType: 'blob'
    })
}

export async function exportWord() {
    return request({
        url: '/infra/db-doc/export-word',
        method: 'get',
        responseType: 'blob'
    })
}

export async function exportMarkdown() {
    return request({
        url: '/infra/db-doc/export-markdown',
        method: 'get',
        responseType: 'blob'
    })
}
