<template>
    <div id="app">
        <div class="row p-3">
            <!-- public part -->
            <div class="col-2">
                <router-link to='/' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">主页</button>
                </router-link>
            </div>
            <div class="col-2">
                <router-link to='/library' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">图书</button>
                </router-link>
            </div>

            <!-- user part -->
            <div v-if="isUser" class="col-2">
                <router-link to='/order' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">订单</button>
                </router-link>
            </div>
            <div v-if="isUser" class="col-2">
                <router-link to='/cart' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">购物车</button>
                </router-link>
            </div>
            <div v-if="isUser" class="offset-2 col-2">
                <!-- click here to exit -->
                <router-link to='/' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block"
                            v-on:mouseover="setExitText()"
                            v-on:mouseout="resetExitText()"
                            v-on:click="sendExitRequest()">{{ exitText }}</button>
                </router-link>
            </div>

            <!-- admin part -->
            <div v-if="isAdmin" class="col-2">
                <router-link to='/management' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">管理</button>
                </router-link>
            </div>
            <div v-if="isAdmin" class="offset-4 col-2">
                <!-- click here to exit -->
                <router-link to='/' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block"
                            v-on:mouseover="setExitText()"
                            v-on:mouseout="resetExitText()"
                            v-on:click="sendExitRequest()">{{ exitText }}</button>
                </router-link>
            </div>

            <!-- visitor part -->
            <div v-if="isVisitor" class="offset-4 col-2">
                <router-link to='/login' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">登录</button>
                </router-link>
            </div>
            <div v-if="isVisitor" class="col-2">
                <router-link to='/register' class="text-decoration-none">
                    <button class="btn btn-implicit-dark btn-block">注册</button>
                </router-link>
            </div>
        </div>
        <router-view v-on:identity-update="updateIdentity()"/>
    </div>
</template>

<style scoped>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }
</style>

<script>
    export default {
        components: {},
        data: function () {
            return {
                userGroup: 'visitor',
                username: '',
                identityUpdateComplete: false,
                exitHover: false
            };
        },
        created: function () {
            this.lazyUpdateIdentity();
        },
        updated: function () {
            this.lazyUpdateIdentity();
        },
        methods: {
            lazyUpdateIdentity: function () {
                if(this.$flags.identityNeedsUpdate){
                    this.updateIdentity();
                    this.$flags.identityNeedsUpdate = false;
                }
            },
            updateIdentity: function () {
                this.identityUpdateComplete = false;
                this.$http.post(
                    '/ebook/identity'
                ).then(
                    (resp)=>{
                        this.userGroup = resp.data.identity;
                        this.username = resp.data.username;
                        this.identityUpdateComplete = true;
                    }, ()=>{
                        this.identityUpdateComplete = true;
                    }
                );
            },
            sendExitRequest: function () {
                this.identityUpdateComplete = false;
                this.$http.post(
                    '/ebook/identity/exit'
                ).then(
                    ()=>{
                        this.resetExitText();
                        this.userGroup = 'visitor';
                        this.username = '';
                        this.identityUpdateComplete = true;
                    }, ()=>{}
                );
            },
            setExitText: function () {
                this.exitHover = true;
            },
            resetExitText: function () {
                this.exitHover = false;
            }
        },
        computed: {
            isVisitor: function () {
                return this.userGroup === 'visitor';
            },
            isUser: function () {
                return this.userGroup === 'user';
            },
            isAdmin: function () {
                return this.userGroup === 'admin';
            },
            welcomeMsg: function () {
                if(!this.identityUpdateComplete){
                    return '加载身份信息……';
                }
                if(this.isUser){
                    return '欢迎，用户 ' + this.username;
                }
                if(this.isAdmin){
                    return '欢迎，管理员 ' + this.username;
                }
                return '';
            },
            exitText: function () {
                return this.exitHover ? '退出登录' : this.welcomeMsg;
            }
        }
    };
</script>
