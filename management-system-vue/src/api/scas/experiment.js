import request from '@/utils/request'

export function getDetail(id) {
    return request({
        url: '/scas/experiment/generate-pdf',
        method: 'get',
        params: {id: id},
        responseType: "blob"
    })
}

// 查询实验报告列表
export function getExperiment(query) {
    return request({
        url: '/scas/experiment/get',
        method: 'get',
        params: query
    })
}

// 新增实验报告
export function addExperiment(data) {
    return request({
        url: '/scas/experiment/add',
        method: 'post',
        data: data
    })
}

// 修改实验报告
export function updateExperiment(data) {
    return request({
        url: '/scas/experiment/update',
        method: 'put',
        data: data
    })
}

export function updateHandle(data) {
    return request({
        url: '/scas/experiment/updateHandle',
        method: 'put',
        data: data
    })
}

// 删除实验报告
export function delExperiment(id) {
    return request({
        url: '/scas/experiment/delete/' + id,
        method: 'delete'
    })
}
