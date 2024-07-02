import request from '@/utils/request'

export function findCategoryTree(keyword= "") {
    return request({
        url: '/course/category/tree?keyword=' + keyword,
        method: 'get'
    })
}

export function saveUpdateCategory(category) {
    return request({
        url: '/course/category/saveupdate',
        method: 'post',
        data: category
    })
}

export function deleteCategoryById(id) {
    return request({
        url: `/course/category/delete/${id}`,
        method: 'post'
    })
}

export function deleteCategoryBatch(batchIds) {
    return request({
        url: '/course/category/saveupdate',
        method: 'post',
        data: {batchIds}
    })
}
