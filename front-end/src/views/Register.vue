<template>
    <div>
        <div class="text-center p-3">
            <img class="mb-4" width="72" height="72" alt="ebook logo" src="../assets/logo.png">
            <h1 class="mb-2">注册</h1>
        </div>
        <RegisterForm></RegisterForm>
    </div>
</template>

<script>
    import RegisterForm from '@/components/RegisterForm';
    export default {
        name: 'Register',
        components: {
            RegisterForm
        }
    }
</script>

<style scoped>
</style>
