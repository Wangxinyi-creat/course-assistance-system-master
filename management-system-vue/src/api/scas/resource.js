import request from '@/utils/request'

// 查询课程资源列表
export function getResource(query) {
    return request({
        url: '/scas/resource/get',
        method: 'get',
        params: query
    })
}

// 新增课程资源
export function addResource(data) {
    return request({
        url: '/scas/resource/add',
        method: 'post',
        data: data
    })
}

// 修改课程资源
export function updateResource(data) {
    return request({
        url: '/scas/resource/update',
        method: 'put',
        data: data
    })
}

// 删除课程资源
export function delResource(id) {
    return request({
        url: '/scas/resource/delete/' + id,
        method: 'delete'
    })
}
