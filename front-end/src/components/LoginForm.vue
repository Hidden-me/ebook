<template>
    <div class="text-center">
        <div class="input-group p-1 text-center">
            <input type="text" class="form-control col-4 m-auto" placeholder="用户名" v-model="username"/>
        </div>
        <div class="input-group p-1">
            <input type="password" class="form-control col-4 m-auto" placeholder="密码" v-model="password"/>
        </div>
        <div class="input-group pt-3">
            <label class="col-4 m-auto">
                <input type="checkbox" value="remember_me"/>
                记住我
            </label>
        </div>
        <div class="input-group p-1 mt-2">
            <button class="btn btn-outline-primary btn-block col-4 m-auto" v-on:click="submitForm()">
                {{ submitText }}
            </button>
        </div>
        <div v-if="validating">
            <span class="text-info">登录认证中……</span>
        </div>
        <div v-else-if="validationNoReply">
            <span class="text-danger">登录认证无响应</span>
        </div>
        <div v-else-if="validationDenied">
            <span class="text-danger">{{ message }}</span>
        </div>
    </div>
</template>

<script>
    import md5 from 'js-md5';
    export default {
        name: 'LoginForm',
        data: function () {
            return {
                username: '',
                password: '',
                pubkey: '',
                message: '',
                validating: false,
                validationDenied: false,
                validationNoReply: false
            };
        },
        computed: {
            submitText: function () {
                return (this.validationDenied ? '重新登录' : '登录');
            }
        },
        methods: {
            submitForm: function() {
                this.$http.post(
                    'login/pubkey',
                    {
                        username: this.username
                    }
                ).then(
                    (resp)=>{
                        this.pubkey = resp.data.pubkey;
                        this.requestValidation();
                    }, ()=>{}
                );
            },
            requestValidation: function () {
                this.validating = true;
                this.$http.post(
                    'login/validate',
                    {
                        username: this.username,
                        token: md5(this.pubkey + md5(this.username + md5(this.password)))
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'success'){
                            // app will update the identity
                            this.$flags.identityNeedsUpdate = true;
                            this.validationDenied = false;
                            // redirect to the home page
                            this.$router.push({name: 'home'});
                        }else{
                            this.message = resp.data.message;
                            this.validationDenied = true;
                        }
                        this.validationNoReply = false;
                        this.validating = false;
                    }, ()=>{
                        // prompt the failure
                        this.validationDenied = false;
                        this.validationNoReply = true;
                        this.validating = false;
                    }
                );
            }
        }
    }
</script>

<style scoped>

</style>
