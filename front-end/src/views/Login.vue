<template>
    <div class="text-center">
        <div class="p-3">
            <img class="mb-4" width="72" height="72" alt="ebook logo" src="../assets/logo.png">
            <h1 class="mb-2">登录</h1>
        </div>
        <LoginForm></LoginForm>
    </div>
</template>

<script>
    import LoginForm from '@/components/LoginForm';
    export default {
        name: 'Login',
        components: {
            LoginForm
        }
    }
</script>

<style scoped>
</style>
