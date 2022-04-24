import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {path: '/login', component: () => import('@/views/login/index'), hidden: true},
  {path: '/404', component: () => import('@/views/404'), hidden: true},

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: {title: 'External Link', icon: 'link'}
      }
    ]
  },

  {path: '*', redirect: '/404', hidden: true},

  {
    path: '/hospSet',
    component: Layout,
    redirect: '/hospSet/hospital/list',
    name: 'hospital',
    meta: {title: '医院管理', icon: 'example'},
    children: [
      {
        path: 'hospitalSet/list',
        name: '医院设置查看',
        component: () => import('@/views/hosp/hospitalSet/list'),
        meta: {title: '查看', icon: 'table'}
      },
      {
        path: 'hospitalSet/add',
        name: '医院设置添加',
        component: () => import('@/views/hosp/hospitalSet/form'),
        meta: {title: '添加', icon: 'table'}
      },
      {
        path: 'hospitalSet/edit/:id',
        name: '医院设置修改',
        component: () => import('@/views/hosp/hospitalSet/form'),
        meta: {title: '编辑', noCache: true},
        hidden: true
      },
      {
        path: 'hosp/list',
        name: '医院列表',
        component: () => import('@/views/hosp/hospital/list'),
        meta: {title: '医院列表', icon: 'table'}
      },
      {
        path: 'hospital/show/:id',
        name: '查看',
        component: () => import('@/views/hosp/hospital/show'),
        meta: {title: '查看', noCache: true},
        hidden: true
      },
      {
        path: 'hospital/schedule/:hoscode',
        name: '排班',
        component: () => import('@/views/hosp/hospital/schedule'),
        meta: {title: '排班', noCache: true},
        hidden: true
      }
    ]
  },

  {
    path: '/cmn',
    component: Layout,
    redirect: '/cmn/list',
    name: '数据管理',
    alwaysShow: true,
    meta: {title: '数据管理', icon: 'example'},
    children: [
      {
        path: 'list',
        name: '数据字典',
        component: () => import('@/views/dict/list'),
        meta: {title: '数据字典', icon: 'table'}
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/userInfo/list',
    name: 'userInfo',
    meta: {title: '用户管理', icon: 'table'},
    alwaysShow: true,
    children: [
      {
        path: 'userInfo/list',
        name: '用户列表',
        component: () => import('@/views/user/userInfo/list'),
        meta: {title: '用户列表', icon: 'table'}
      },
      {
        path: 'userInfo/show/:id',
        name: '用户查看',
        component: () => import('@/views/user/userInfo/show'),
        meta: {title: '用户查看'},
        hidden: true
      },
      {
        path: 'userInfo/authList',
        name: '认证审批列表',
        component: () => import('@/views/user/userInfo/authList'),
        meta: {title: '认证审批列表', icon: 'table'}
      }
    ]
  }

  // {
  //   path: '/statistics',
  //   component: Layout,
  //   redirect: '/statistics/order/index',
  //   name: 'BasesInfo',
  //   meta: {title: '统计管理', icon: 'table'},
  //   alwaysShow: true,
  //   children: [
  //     {
  //       path: 'order/index',
  //       name: '预约统计',
  //       component: () => import('@/views/statistics/order/index'),
  //       meta: {title: '预约统计'}
  //     }
  //   ]
  // },
  // {
  //   path: '/order',
  //   component: Layout,
  //   redirect: '/order/orderInfo/list',
  //   name: 'BasesInfo',
  //   meta: {title: '订单管理', icon: 'table'},
  //   alwaysShow: true,
  //   children: [
  //     {
  //       path: 'orderInfo/list',
  //       name: '订单列表',
  //       component: () => import('@/views/order/orderInfo/list'),
  //       meta: {title: '订单列表'}
  //     },
  //
  //     {
  //       path: 'orderInfo/show/:id',
  //       name: '查看',
  //       component: () => import('@/views/order/orderInfo/show'),
  //       meta: {title: '查看', noCache: true},
  //       hidden: true
  //     }
  //
  //
  //   ]
  // }


]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
