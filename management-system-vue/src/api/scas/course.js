import request from '@/utils/request'

// 查询课程列表
export function getCourse(query) {
    return request({
        url: '/scas/course/get',
        method: 'get',
        params: query
    })
}

export function getDetail(id) {
    return request({
        url: '/scas/course/getDetail',
        method: 'get',
        params: {id: id}
    })
}

// 查询课程列表-个人创建  1-仅查询个人创建的课程  2-查询所有课程
export function getCreate(status) {
    return request({
        url: '/scas/course/getCreate',
        method: 'get',
        params: {status: status}
    })
}

// 新增课程
export function addCourse(data) {
    return request({
        url: '/scas/course/add',
        method: 'post',
        data: data
    })
}

// 修改课程
export function updateCourse(data) {
    return request({
        url: '/scas/course/update',
        method: 'put',
        data: data
    })
}

// 删除课程
export function delCourse(id) {
    return request({
        url: '/scas/course/delete/' + id,
        method: 'delete'
    })
}
