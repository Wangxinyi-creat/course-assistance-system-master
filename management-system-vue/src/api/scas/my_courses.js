import request from '@/utils/request'

// 查询我的课程列表
export function getCourses(query) {
    return request({
        url: '/scas/courses/get',
        method: 'get',
        params: query
    })
}

// 新增我的课程
export function addCourses(data) {
    return request({
        url: '/scas/courses/add',
        method: 'post',
        data: data
    })
}

// 修改我的课程
export function updateCourses(data) {
    return request({
        url: '/scas/courses/update',
        method: 'put',
        data: data
    })
}

// 删除我的课程
export function delCourses(id) {
    return request({
        url: '/scas/courses/delete/' + id,
        method: 'delete'
    })
}
