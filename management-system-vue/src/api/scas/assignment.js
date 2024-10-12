import request from '@/utils/request'

// 查询作业列表
export function getAssignment(query) {
    return request({
        url: '/scas/assignment/get',
        method: 'get',
        params: query
    })
}

// 新增作业
export function addAssignment(data) {
    return request({
        url: '/scas/assignment/add',
        method: 'post',
        data: data
    })
}

// 修改作业
export function updateAssignment(data) {
    return request({
        url: '/scas/assignment/update',
        method: 'put',
        data: data
    })
}

export function updateHandle(data) {
    return request({
        url: '/scas/assignment/updateHandle',
        method: 'put',
        data: data
    })
}

// 删除作业
export function delAssignment(id) {
    return request({
        url: '/scas/assignment/delete/' + id,
        method: 'delete'
    })
}
