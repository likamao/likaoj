<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <div class="title-bar">
          <a-avatar class="logo">
            <img class="logo" alt="logo" src="../assets/logo.png" />
          </a-avatar>
          <div class="title">LIKAOJ</div>
        </div>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://github.com/likaboy">LIKA</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#userLayout {
}

.title-bar {
  display: flex;
  align-items: center;
  text-align: center;
  padding: 10px;
  justify-content: center;
  height: 100px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.logo {
  height: 40px;
  margin-right: 10px;
}

#userLayout .header {
  margin-bottom: 10px;
  box-shadow: #eee 1px 1px 5px;
}

#userLayout .content {
  background: #fff;
  margin-bottom: 16px;
  padding: 20px;
}

#userLayout .footer {
  background: #efefef;
  padding: 14px;
  position: sticky;
  bottom: 0;
  right: 0;
  left: 0;
  text-align: center;
}
</style>

<script setup lang="ts"></script>
