import request from '@/utils/request'

// 查询通知公告列表
export function getNotice(query) {
    return request({
        url: '/notice/get',
        method: 'get',
        params: query
    })
}

// 查询通知公告列表--通过id
export function getNoticeById(noticeId) {
    return request({
        url: '/notice/get/' + noticeId,
        method: 'get'
    })
}

// 新增通知公告
export function addNotice(data) {
    return request({
        url: '/notice/add',
        method: 'post',
        data: data
    })
}

// 修改通知公告
export function updateNotice(data) {
    return request({
        url: '/notice/update',
        method: 'put',
        data: data
    })
}

/**
 * 修改通知公告状态
 * @param noticeId
 * @param status
 * @returns {*}
 */
export function changeStatus(noticeId, status) {
    return request({
        url: '/notice/changeStatus/' + noticeId + "/" + status,
        method: 'POST',
    });
}

// 删除通知公告
export function delNotice(noticeId) {
    return request({
        url: '/notice/delete/' + noticeId,
        method: 'delete'
    })
}