import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/admin/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/admin/dashboard',
    hidden: true,
    children: [
      {
        path: '/admin/dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'Dashboard', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/documentation',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/documentation/index'),
        name: 'Documentation',
        meta: { title: 'Documentation', icon: 'documentation', affix: true }
      }
    ]
  },
  {
    path: '/guide',
    component: Layout,
    redirect: '/guide/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/guide/index'),
        name: 'Guide',
        meta: { title: 'Guide', icon: 'guide', noCache: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'Profile', icon: 'user', noCache: true }
      }
    ]
  },
  {
    path: '/shop',
    component: () => import('@/layout-web'),
    redirect: '/shop/home',
    hidden: true,
    children: [
      {
        path: 'home',
        component: () => import('@/views-system/web-view/home'),
        name: 'Home',
        meta: { title: 'Home', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'login',
        component: () => import('@/views-system/web-view/login'),
        name: 'Login',
        meta: { title: 'Login', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'product/:id',
        component: () => import('@/views-system/web-view/product'),
        name: 'Product',
        meta: { title: 'Product', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'pay',
        component: () => import('@/views-system/web-view/pay'),
        name: 'Pay',
        meta: { title: 'Pay', icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'user/info',
        component: () => import('@/views-system/web-view/user/info'),
        name: 'user-info',
        meta: { icon: 'user', noCache: true },
        hidden: true
      },
      {
        path: 'user/order',
        component: () => import('@/views-system/web-view/user/order'),
        name: 'user-order',
        meta: { icon: 'user', noCache: true },
        hidden: true
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/admin/sales-at-the-counter',
    component: Layout,
    redirect: '/admin/sales-at-the-counter',
    name: 'sales-at-the-counter',
    meta: {
      title: 'Bán hàng tại quầy',
      icon: 'component',
      roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES']
    },
    children: [
      {
        path: 'sales',
        component: () => import('@/views-system/cms/sales-at-the-counter/index.vue'),
        name: 'sales-at-the-counter-page',
        meta: { title: 'Bán hàng tại quầy', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      }
    ]
  },
  {
    path: '/admin/product-management',
    component: Layout,
    redirect: '/admin/product-management/product',
    name: 'product-management',
    meta: {
      title: 'Quản lý Sản phẩm',
      icon: 'component',
      roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES']
    },
    children: [
      {
        path: 'product',
        component: () => import('@/layout-empty'),
        redirect: '/admin/product-management/product/list',
        name: 'product-management-product-page',
        meta: { title: 'Sản phẩm', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] },
        children: [
          {
            path: 'list',
            component: () => import('@/views-system/cms/product-management/product/index.vue'),
            name: 'product-management-product-detail-list-page',
            meta: { title: 'Sản phẩm', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
          },
          {
            path: 'detail',
            component: () => import('@/views-system/cms/product-management/product/product-detail.vue'),
            name: 'product-management-product-detail-detail-page',
            meta: { title: 'Chi tiết Sản phẩm', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] },
            hidden: true
          }
        ]
      },
      {
        path: 'category/list',
        component: () => import('@/views-system/cms/product-management/category/index.vue'),
        name: 'product-management-category-page',
        meta: { title: 'Danh mục', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      },
      {
        path: 'brand/list',
        component: () => import('@/views-system/cms/product-management/brand/index.vue'),
        name: 'product-management-brand-page',
        meta: { title: 'Thương hiệu', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      },
      {
        path: 'size/list',
        component: () => import('@/views-system/cms/product-management/size/index.vue'),
        name: 'product-management-size-page',
        meta: { title: 'Kích cỡ', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      },
      {
        path: 'color/list',
        component: () => import('@/views-system/cms/product-management/color/index.vue'),
        name: 'product-management-color-page',
        meta: { title: 'Màu sắc', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      },
      {
        path: 'material/list',
        component: () => import('@/views-system/cms/product-management/material/index.vue'),
        name: 'product-management-material-page',
        meta: { title: 'Chất liệu', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      }
    ]
  },
  {
    path: '/admin/voucher-management',
    component: Layout,
    redirect: '/admin/voucher-management/voucher/list',
    name: 'voucher-management',
    meta: {
      title: 'Quản lý Mã giảm giá',
      icon: 'component'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views-system/cms/voucher-management/index.vue'),
        name: 'voucher-management-list-page',
        meta: { title: 'Quản lý Mã giảm giá', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      }
    ]
  },
  {
    path: '/admin/order-management',
    component: Layout,
    redirect: '/admin/order-management/order-online/list-online',
    name: 'order-management',
    meta: {
      title: 'Quản lý Đơn hàng',
      icon: 'component'
    },
    children: [
      {
        path: 'list-online',
        component: () => import('@/views-system/cms/order-management/online/index.vue'),
        name: 'order-online-management-list-page',
        meta: { title: 'Online', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      },
      {
        path: 'list-at-the-shop',
        component: () => import('@/views-system/cms/order-management/at-the-shop/index.vue'),
        name: 'order-at-the-shop-management-list-page',
        meta: { title: 'Tại cửa hàng', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      }
    ]
  },
  {
    path: '/admin/user-management',
    component: Layout,
    redirect: '/admin/user-management/list',
    name: 'user-management',
    meta: {
      title: 'Quản lý Người dùng',
      icon: 'component'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views-system/cms/user-management/customer-at-the-shop.vue'),
        name: 'user-management-list-page',
        meta: { title: 'Quản lý Người dùng', noCache: true, roles: ['ROLE_SUPER_ADMIN', 'ROLE_STAFF_WAREHOUSE_MANAGEMENT', 'ROLE_STAFF_SALES'] }
      }
    ]
  },

  // {
  //   path: '/admin/user-management',
  //   component: Layout,
  //   redirect: '/category',
  //   name: 'user-management',
  //   meta: {
  //     title: 'Quản lý người dùng',
  //     icon: 'component'
  //   },
  //   children: [
  //     {
  //       path: 'customer-at-the-shop',
  //       component: () => import('@/views-system/cms/user-management/customer-at-the-shop.vue'),
  //       name: 'customer-at-the-shop',
  //       meta: { title: 'Khách mua tại của hàng', noCache: true }
  //     },
  //     {
  //       path: 'customer-at-the-shop',
  //       component: () => import('@/views-system/cms/user-management/customer-at-the-shop.vue'),
  //       name: 'customer-at-the-shop',
  //       meta: { title: 'Khách mua tại của hàng', noCache: true }
  //     }
  //   ]
  // },

  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/page',
    alwaysShow: true, // will always show the root menu
    name: 'Permission',
    meta: {
      title: 'Permission',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/permission/page'),
        name: 'PagePermission',
        meta: {
          title: 'Page Permission',
          roles: ['admin'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'directive',
        component: () => import('@/views/permission/directive'),
        name: 'DirectivePermission',
        meta: {
          title: 'Directive Permission'
          // if do not set roles, means: this page does not require permission
        }
      },
      {
        path: 'role',
        component: () => import('@/views/permission/role'),
        name: 'RolePermission',
        meta: {
          title: 'Role Permission',
          roles: ['admin']
        }
      }
    ]
  },

  {
    path: '/icon',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/icons/index'),
        name: 'Icons',
        meta: { title: 'Icons', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/list',
    name: 'Example',
    meta: {
      title: 'Example',
      icon: 'el-icon-s-help'
    },
    hidden: true,
    children: [
      {
        path: 'create',
        component: () => import('@/views/example/create'),
        name: 'CreateArticle',
        meta: { title: 'Create Article', icon: 'edit' }
      },
      {
        path: 'edit/:id(\\d+)',
        component: () => import('@/views/example/edit'),
        name: 'EditArticle',
        meta: { title: 'Edit Article', noCache: true, activeMenu: '/example/list' },
        hidden: true
      },
      {
        path: 'list',
        component: () => import('@/views/example/list'),
        name: 'ArticleList',
        meta: { title: 'Article List', icon: 'list' }
      }
    ]
  },

  {
    path: '/tab',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/tab/index'),
        name: 'Tab',
        meta: { title: 'Tab', icon: 'tab' }
      }
    ]
  },

  {
    path: '/error',
    component: Layout,
    redirect: 'noRedirect',
    name: 'ErrorPages',
    hidden: true,
    meta: {
      title: 'Error Pages',
      icon: '404'
    },
    children: [
      {
        path: '401',
        component: () => import('@/views/error-page/401'),
        name: 'Page401',
        meta: { title: '401', noCache: true }
      },
      {
        path: '404',
        component: () => import('@/views/error-page/404'),
        name: 'Page404',
        meta: { title: '404', noCache: true }
      }
    ]
  },

  {
    path: '/error-log',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: () => import('@/views/error-log/index'),
        name: 'ErrorLog',
        meta: { title: 'Error Log', icon: 'bug' }
      }
    ]
  },

  {
    path: '/excel',
    component: Layout,
    redirect: '/excel/export-excel',
    name: 'Excel',
    meta: {
      title: 'Excel',
      icon: 'excel'
    },
    hidden: true,
    children: [
      {
        path: 'export-excel',
        component: () => import('@/views/excel/export-excel'),
        name: 'ExportExcel',
        meta: { title: 'Export Excel' }
      },
      {
        path: 'export-selected-excel',
        component: () => import('@/views/excel/select-excel'),
        name: 'SelectExcel',
        meta: { title: 'Export Selected' }
      },
      {
        path: 'export-merge-header',
        component: () => import('@/views/excel/merge-header'),
        name: 'MergeHeader',
        meta: { title: 'Merge Header' }
      },
      {
        path: 'upload-excel',
        component: () => import('@/views/excel/upload-excel'),
        name: 'UploadExcel',
        meta: { title: 'Upload Excel' }
      }
    ]
  },

  {
    path: '/zip',
    component: Layout,
    redirect: '/zip/download',
    alwaysShow: true,
    name: 'Zip',
    meta: { title: 'Zip', icon: 'zip' },
    hidden: true,
    children: [
      {
        path: 'download',
        component: () => import('@/views/zip/index'),
        name: 'ExportZip',
        meta: { title: 'Export Zip' }
      }
    ]
  },

  {
    path: '/pdf',
    component: Layout,
    redirect: '/pdf/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/pdf/index'),
        name: 'PDF',
        meta: { title: 'PDF', icon: 'pdf' }
      }
    ]
  },
  {
    path: '/pdf/download',
    component: () => import('@/views/pdf/download'),
    hidden: true
  },

  {
    path: '/theme',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/theme/index'),
        name: 'Theme',
        meta: { title: 'Theme', icon: 'theme' }
      }
    ]
  },

  {
    path: '/clipboard',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/clipboard/index'),
        name: 'ClipboardDemo',
        meta: { title: 'Clipboard', icon: 'clipboard' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'https://github.com/PanJiaChen/vue-element-admin',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
