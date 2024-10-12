import request from '@/utils/request'

// 查询课程评价列表
export function getReviews(query) {
    return request({
        url: '/scas/reviews/get',
        method: 'get',
        params: query
    })
}

// 新增课程评价
export function addReviews(data) {
    return request({
        url: '/scas/reviews/add',
        method: 'post',
        data: data
    })
}

// 修改课程评价
export function updateReviews(data) {
    return request({
        url: '/scas/reviews/update',
        method: 'put',
        data: data
    })
}

// 删除课程评价
export function delReviews(id) {
    return request({
        url: '/scas/reviews/delete/' + id,
        method: 'delete'
    })
}
