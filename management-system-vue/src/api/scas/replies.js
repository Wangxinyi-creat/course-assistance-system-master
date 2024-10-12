import request from '@/utils/request'

// 查询提问回复列表
export function getReplies(query) {
    return request({
        url: '/scas/replies/get',
        method: 'get',
        params: query
    })
}

// 新增提问回复
export function addReplies(data) {
    return request({
        url: '/scas/replies/add',
        method: 'post',
        data: data
    })
}

// 提问回复
export function replyReplies(data) {
    return request({
        url: '/scas/replies/reply',
        method: 'put',
        data: data
    })
}

// 修改提问回复
export function updateReplies(data) {
    return request({
        url: '/scas/replies/update',
        method: 'put',
        data: data
    })
}

// 删除提问回复
export function delReplies(replyId) {
    return request({
        url: '/scas/replies/delete/' + replyId,
        method: 'delete'
    })
}
