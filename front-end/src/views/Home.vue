<template>
    <div class="p-3">
        <img class="mb-4" width="72" height="72" alt="ebook logo" src="../assets/logo.png">
        <div class="text-center">
            <h1 class="display-4 p-3">e-Book</h1>
            <h2 class="p-2">专业的在线书店</h2>
        </div>
        <hr />
        <Features></Features>
        <hr />
        <div class="p-2">
            <router-link to="/library">
                <button class="btn btn-outline-primary col-4">进店看看</button>
            </router-link>
        </div>
    </div>
</template>

<script>
    import Features from '@/components/Features.vue';
    export default {
        name: 'home',
        components: {
            Features
        }
    }
</script>
