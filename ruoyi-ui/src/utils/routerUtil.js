import { axios } from '@/utils/request'
// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView, BlankLayout, PageView } from '@/layouts'
import { defaultRouterMap } from '@/config/router.config'

// 前端路由表
const constantRouterComponents = {
  // 基础页面 layout 必须引入
  BasicLayout: BasicLayout, // 基础页面布局，包含了头部导航，侧边栏和通知栏
  BlankLayout: BlankLayout, // 空白的布局
  RouteView: RouteView, // 空布局，专门为了二级菜单内容区自定义
  PageView: PageView, // 基础布局，包含了面包屑，和中间内容区 (slot)
  UserLayout: UserLayout // 登陆注册页面的通用布局
}

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
  path: '*', redirect: '/404', hidden: true
}

// 根级菜单
const rootRouter = {
  'title': 'menu.home',
  'key': '',
  'name': 'index',
  'component': 'BasicLayout',
  'redirect': '/dashboard/analysis', // 登录跳转
  'children': []
}

/**
 * 获取后端路由信息的 axios API
 * @returns {Promise}
 */
export const getRouterByUser = () => {
  return axios({
    url: '/system/menu/user',
    method: 'get',
    dataType: 'json'
    /* headers: {
      'Access-Token': 'xxx'
    }
    */
  })
}

/**
 * 获取路由菜单信息
 *
 * 1. 调用 getRouterByUser() 访问后端接口获得路由结构数组
 *    @see https://github.com/sendya/ant-design-pro-vue/blob/feature/dynamic-menu/public/dynamic-menu.json
 * 2. 调用
 * @returns {Promise<any>}
 */
export const generatorDynamicRouter = () => {
  return new Promise((resolve, reject) => {
    // ajax
    getRouterByUser().then(res => {
      const rootMenu = getRootMenu(res)
      const routers = generator(rootMenu)
      // console.log('routers', routers)
      routers.push(notFoundRouter)
      resolve(routers)
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 格式化 后端 结构信息并递归生成层级路由表
 *
 * @param routerMap
 * @param parent
 * @returns {*}
 */
export const generator = (routerMap, parent) => {
  return routerMap.map(item => {
    const currentRouter = {
      // 路由地址 动态拼接生成如 /dashboard/workplace
      path: item.path || `${parent && parent.path || ''}/${item.key}`,
      // 路由名称，建议唯一
      name: item.name || item.key || '',
      // 该路由对应页面的 组件 优先根据组件名或者key从constantRouterComponents获取，没有则通过组件名地址查询
      component: constantRouterComponents[item.component || item.key] || loadView(item.component),
      hideChildrenInMenu: item.hideChildrenInMenu || false,
      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: { title: item.title, icon: item.icon || undefined, hiddenHeaderContent: item.hiddenHeaderContent || false, target: item.target }
    }
    // 隐藏菜单
    if (item.hidden) {
      currentRouter.hidden = true
    }
    // 是否设置了隐藏子菜单
    if (item.hideChildrenInMenu) {
      currentRouter.hideChildrenInMenu = true
    }
    // 为了防止出现后端返回结果不规范，处理有可能出现拼接出两个 反斜杠
    if (!currentRouter.path.startsWith('http')) {
      currentRouter.path = currentRouter.path.replace('//', '/')
    }
    // 重定向
    item.redirect && (currentRouter.redirect = item.redirect)
    // 是否有子菜单，并递归处理
    if (item.children && item.children.length > 0) {
      // Recursion
      currentRouter.children = generator(item.children, currentRouter)
    }
    return currentRouter
  })
}
export const loadView = (view) => { // 路由懒加载
  return () => import(`@/views/${view}`)
}

export function getRootMenu (rows) {
  // 根菜单
  const rootMenu = []
  const arr = []
  const menus = defaultRouterMap
  buildtree(rows, arr, 0)
  arr.forEach(row => {
    menus.push(row)
  })
  rootRouter.children = menus
  rootMenu.push(rootRouter)
  return rootMenu
}

export function buildtree (list, arr, parentId) {
  list.forEach(item => {
    if (item.parentId === parentId) {
      var child = {
        title: item.menuName,
        key: item.menuKey,
        icon: item.icon,
        hidden: item.visible === '1',
        path: item.path && item.path.length > 0 ? item.path : undefined,
        component: item.component,
        redirect: item.redirect,
        target: item.target,
        hideChildrenInMenu: item.hiddenChildren,
        hiddenHeaderContent: item.hiddenHeader,
        children: []
      }
      buildtree(list, child.children, item.menuId)
      if (child.children.length === 0) {
        delete child.children
      }
      arr.push(child)
    }
  })
}
