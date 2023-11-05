import Vue from 'vue/dist/vue.esm.js'
import Router from 'vue-router'
import Login from '@/views/Login'
import Home from '@/views/Home'
import DateList from '@/components/dataList'
import HandUpdate from '@/components/handupdate'
import personPage from '@/components/personPage'
import Map from '@/components/map'
import ChooseUpdate from '@/components/chooseupdate'
import CodeUpdate from '@/components/codeupdate'
import DataStatistic from '@/components/DataStatistic'

Vue.use(Router)

export const constantRouterMap = [
  {
    path: '/',
    redirect: '/Login',
  },
  {
    name: 'login',
    path: '/login',
    component: Login,
  },
  {
    name: 'home',
    path: '/home',

    component: Home,
    redirect: '/dataList',
    children: [
      {
        name: 'dataList',
        path: '/dataList',
        component: DateList,
        meta: { stitle: '数据列表', ftitle: '数据分析及统计' },
      },
      {
        path: '/handupdate',
        component: HandUpdate,
        meta: { stitle: '手动输入', ftitle: '灾情报送' },
        redirect: '/handupdate/chooseupdate',
        children: [
          {
            path: '/handupdate/chooseupdate',
            component: ChooseUpdate,
            meta: { stitle: '手动输入', ftitle: '灾情报送' },
          },
          {
            path: '/handupdate/codeupdate',
            component: CodeUpdate,
            meta: { stitle: '手动输入', ftitle: '灾情报送' },
          },
        ],
      },
      {
        path: '/map',
        component: Map,
        meta: { stitle: '可视化地图', ftitle: '数据分析及统计' },
      },
      {
        path: '/datastatistic',
        component: DataStatistic,
        meta: { stitle: '数据统计', ftitle: '数据分析及统计' },
      },
      {
        name: 'personPage',
        path: '/personPage',
        component: personPage,
        meta: {stitle: '账户管理', ftitle: '个人中心'}
      }
    ],
  },
]

const router = new Router({
  routes: constantRouterMap,
})

export default router
