<template>
  <div class="container">
    <el-header class="header" height="this.children.height" style="background-image: linear-gradient(to bottom , #d9ecff, #ecf5ff);" v-if="Aside" v-show="Aside" transition="slide-fade">
      <Header></Header>
    </el-header>
    <el-container>
      <template v-if="shouldShowLayout">
        <el-aside class="aside" ref="aside" transition="slide-fade">
          <Aside></Aside>
        </el-aside>
      </template>

      <el-main class="main" :style="mainStyle" >
        <transition name="el-zoom-in-center">
          <router-view></router-view>
        </transition>
      </el-main>
    </el-container>
  </div>
</template>

<script>

import Aside from "@/components/Layout/Aside.vue";
import Header from "@/components/Layout/Header.vue";


export default {
  name: 'App',
  components: {
    Header,
    Aside
  },
  data() {
    return {
      asideWidth: '0px'
    }
  },

  mounted() {
    this.$nextTick(() => {
      if (this.$refs.aside) {
        this.asideWidth = this.$refs.aside.$el.clientWidth + 'px';
      }
    });
  },

  created() {
    //在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem('store')) {
      this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem('store'))));
    }

    //在页面刷新时将vuex里的信息保存到sessionStorage里
    window.addEventListener('beforeunload', () => {
      sessionStorage.setItem('store', JSON.stringify(this.$store.state));
    });
  },
  computed: {
    isMobile: () => {
      return (window.screen.availWidth < 768)
    },
    shouldShowLayout() {
      return this.$route.name !== 'Login' && this.$route.name !== 'Register' && !this.isMobile;
    },
    Aside() {
      return this.$route.name !== 'Login' && this.$route.name !== 'Register'
    },
    mainStyle() {
      return {
        "background-color":"#F6F8FA",
        'margin-left': this.shouldShowLayout ? this.asideWidth : 0,
        'height': this.shouldShowLayout ? `calc(100vh - ${this.headerHeight}px)` : '100vh',
      }
    },
    headerHeight() {
      return this.shouldShowLayout ? 60 : 0;
    }
  }


}
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
  'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

.container {
  height: 100%;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
}

.aside {
  position: fixed;
  top: 60px;
  left: 0;
  bottom: 0;
  z-index: 999;
  overflow: auto;
}

.main {
  height: calc(100vh - 60px);
  overflow-y: auto;
  margin-top: 60px;
  margin-left: 240px; /* 侧栏宽度 */
}

@media screen and (max-width: 767px) {
  .main {
    margin-left: 0;
  }
}


/* 隐藏滚动条 */
::-webkit-scrollbar {
  width: 0;
}

::-webkit-scrollbar-track {
  background-color: #F6F8FA;
}

</style>
