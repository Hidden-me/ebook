<template>
    <div>
        <div class="bg-primary text-white p-3 mb-3">用户管理</div>
        <div v-if="gettingUserList">获取用户列表中……</div>
        <div v-else-if="getUserListFail">获取用户列表失败……</div>
        <div v-else>
            <UserItem v-for="(user, index) in users" v-bind:key="index" v-bind:user="user"></UserItem>
        </div>
    </div>
</template>

<script>
    import UserItem from '@/components/UserItem';
    export default {
        name: 'UserManager',
        components: {UserItem},
        created: function () {
            this.gettingUserList = true;
            this.$http.post(
                'user/list'
            ).then(
                (resp)=>{
                    if(resp.data.result === 'failure'){
                        this.getUserListFail = true;
                    }else{
                        this.users = resp.data.users;
                        this.getUserListFail = false;
                    }
                    this.gettingUserList = false;
                }, ()=>{
                    this.gettingUserList = false;
                }
            );
        },
        data: function () {
            return {
                gettingUserList: false,
                getUserListFail: false,
                users: [
                    // all users here are customers (non-admin)
                    // admins cannot see admins in the list
                    // {username: 'Alice', uid: 1, banned: false}
                ]
            };
        }
    }
</script>

<style scoped>

</style>
