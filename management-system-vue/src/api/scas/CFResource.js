import request from '@/utils/request'

// 推荐学习资源
export function getRecommendResource() {
    return request({
        url: '/scas/cf/get',
        method: 'get'
    })
}
