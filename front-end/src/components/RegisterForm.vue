<template>
    <div class="text-center">
        <div class="input-group p-1">
            <input type="text" class="form-control col-4 m-auto" placeholder="用户名" v-model="username"/>
        </div>
        <div class="input-group p-1">
            <input type="password" class="form-control col-4 m-auto" placeholder="密码" v-model="password"/>
        </div>
        <div class="input-group p-1">
            <input type="password" class="form-control col-4 m-auto" placeholder="重复密码" v-model="repeatPassword"/>
        </div>
        <div class="input-group p-1">
            <input type="text" class="form-control col-4 m-auto" placeholder="电子邮箱" v-model="email"/>
        </div>
        <div class="input-group pt-3">
            <label class="col-4 m-auto">
                <input type="checkbox" value="auto_login" v-model="autoLogin"/>
                注册完成后自动登录
            </label>
        </div>
        <div class="input-group p-1 mt-2">
            <button class="btn btn-outline-primary btn-block col-4 m-auto" type="submit"
                    v-on:click="submitForm()">注册</button>
        </div>
        <div v-if="registering">
            <span class="text-info">注册中……</span>
        </div>
        <div v-if="formError">
            <span class="text-danger">{{ formErrorMsg }}</span>
        </div>
        <div v-else-if="registrationNoReply">
            <span class="text-secondary">注册无响应</span>
        </div>
        <div v-else-if="registrationFail">
            <span class="text-danger">注册失败：{{ registrationFailMsg }}</span>
        </div>
    </div>
</template>

<script>
    import md5 from 'js-md5';
    export default {
        name: 'RegisterForm',
        data: function () {
            return {
                username: '',
                password: '',
                repeatPassword: '',
                email: '',
                formErrorMsg: '',
                registrationFailMsg: '',
                autoLogin: true,
                registering: false,
                formError: false,
                registrationNoReply: false,
                registrationFail: false
            };
        },
        methods: {
            submitForm: function() {
                this.formError = !this.checkForm();
                if(this.formError){
                    return;
                }
                this.registering = true;
                this.$http.post(
                    'register',
                    {
                        username: this.username,
                        passToken: md5(this.username + md5(this.password)),
                        email: this.email,
                        autoLogin: this.autoLogin
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'success'){
                            if(this.autoLogin){
                                // app will update the identity directly
                                this.$flags.identityNeedsUpdate = true;
                                // redirect to home page
                                this.$router.push({name: 'home'});
                            }else{
                                // redirect to login page
                                this.$router.push({name: 'login'});
                            }
                            this.registrationFail = false;
                        }else{
                            // prompt the failure
                            this.registrationFailMsg = resp.data.message;
                            this.registrationFail = true;
                        }
                        this.registrationNoReply = false;
                        this.registering = false;
                    }, ()=>{
                        this.registrationFail = false;
                        this.registrationNoReply = true;
                        this.registering = false;
                    }
                );
            },
            checkForm: function () {
                this.formError = false;
                // empty/long value check
                if(this.username.length <= 0){
                    this.formErrorMsg = '用户名不能为空';
                    return false;
                }
                if(this.username.length > 24){
                    this.formErrorMsg = '用户名不能超过24位';
                    return false;
                }
                if(this.password.length <= 0){
                    this.formErrorMsg = '密码不能为空';
                    return false;
                }
                if(this.password.length > 24){
                    this.formErrorMsg = '密码不能超过24位';
                    return false;
                }
                if(this.email.length <= 0){
                    this.formErrorMsg = '邮箱不能为空';
                    return false;
                }
                if(this.email.length > 32){
                    this.formErrorMsg = '暂不支持超过32位的邮箱';
                    return false;
                }
                // password consistency check
                if(this.password !== this.repeatPassword){
                    this.formErrorMsg = '密码与重复密码不一致';
                    return false;
                }
                // email format check
                let list = this.email.split('@');
                if(list.length !== 2){
                    this.formErrorMsg = '邮箱格式不正确';
                    return false;
                }
                let userspace = list[0];
                let domain = list[1];
                if(userspace.length <= 0){
                    this.formErrorMsg = '邮箱格式不正确';
                    return false;
                }
                list = domain.split('.');
                if(list.length <= 1){
                    this.formErrorMsg = '邮箱格式不正确';
                    return false;
                }
                for(let ele in list){
                    if(ele.length <= 0){
                        this.formErrorMsg = '邮箱格式不正确';
                        return false;
                    }
                }
                return true;
            }
        }
    }
</script>

<style scoped>

</style>
