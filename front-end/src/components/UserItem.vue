<template>
    <div class="row mt-3 mb-3">
        <div class="col-4 offset-1">
            <div v-if="refreshing">加载用户数据……</div>
            <div v-else-if="refreshFail">加载用户数据失败</div>
            <div v-else>
                <div>{{ user.username }} (UID: {{ user.uid }})</div>
                <div>
                    状态：
                    <span v-bind:class="{'text-danger':user.banned}">
                        {{ status }}
                    </span>
                </div>
            </div>
        </div>
        <button class="btn col-2 offset-1" v-bind:class="{'btn-outline-danger':!user.banned,
            'btn-outline-primary':user.banned}" v-on:click="switchBan()">{{ banText }}</button>
        <button class="btn btn-outline-info col-2 offset-1">发送通知</button>
    </div>
</template>

<script>
    export default {
        name: 'UserItem',
        props: ['user'],
        data: function () {
            return {
                refreshing: false,
                refreshFail: false
            };
        },
        computed: {
            status: function () {
                if(this.user.banned){
                    return '封禁中';
                }
                return '正常';
            },
            banText: function () {
                if(this.user.banned){
                    return '解禁';
                }
                return '封禁';
            }
        },
        methods: {
            switchBan: function () {
                this.$http.post(
                    'user/ban', {
                        uid: this.user.uid
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.refreshInfo();
                        }
                    }, ()=>{}
                );
            },
            refreshInfo: function () {
                this.refreshing = true;
                this.$http.post(
                    'user/refresh', {
                        uid: this.user.uid
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            this.refreshFail = true;
                            this.refreshing = false;
                        }else{
                            this.user = resp.data.user;
                            this.refreshFail = false;
                            this.refreshing = false;
                        }
                    }, ()=>{
                        this.refreshFail = true;
                        this.refreshing = false;
                    }
                );
            }
        }
    }
</script>

<style scoped>

</style>
