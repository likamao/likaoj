import NoAuthView from '@/layout/NoAuthView.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import UserLoginView from '@/views/user/UserLoginView.vue'
import UserRegisterView from '@/views/user/UserRegisterView.vue'
import UserLayout from '@/layout/UserLayout.vue'
import AddQuestionView from '@/views/question/AddQuestionView.vue'
import ManageQuestionView from '@/views/question/ManageQuestionView.vue'
import QuestionsView from '@/views/question/QuestionsView.vue'
import ViewQuestionView from '@/views/question/ViewQuestionView.vue'
import QuestionsSubmitView from '@/views/question/QuestionsSubmitView.vue'

export const routes = [
  {
    path: '/user',
    name: '用户',
    component: UserLayout,
    meta: {
      hideInMenu: true,
    },
    children: [
      {
        path: '/user/login',
        name: '用户登录',
        component: UserLoginView,
      },
      {
        path: '/user/register',
        name: '用户注册',
        component: UserRegisterView,
      },
    ],
  },
  {
    path: '/',
    name: '主页',
    component: QuestionsView,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: '/questions',
    name: '浏览题目',
    component: QuestionsView,
  },
  {
    path: '/question_submit',
    name: '浏览题目提交',
    component: QuestionsSubmitView,
  },
  {
    path: '/view/question/:id',
    name: '做题',
    component: ViewQuestionView,
    props: true,
    meta: {
      hideInMenu: true,
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: '/add/question',
    name: '创建题目',
    component: AddQuestionView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: '/update/question',
    name: '更新题目',
    component: AddQuestionView,
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
  },
  {
    path: '/manage/question',
    name: '管理题目',
    component: ManageQuestionView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: '/noAuth',
    name: '无权限',
    component: NoAuthView,
    meta: {
      hideInMenu: true,
    },
  },
  // {
  //   path: '/admin',
  //   name: '管理员可见',
  //   component: AdminView,
  //   meta: {
  //     access: ACCESS_ENUM.ADMIN,
  //   },
  // },
  // {
  //   path: '/about',
  //   name: '关于我们',
  //   component: () => import('../views/AboutView.vue'),
  // },
]
