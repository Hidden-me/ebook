<template>
    <div>
        <div class="bg-primary text-white p-4">
            <span>选择您想要的图书，加入购物车！</span>
        </div>
        <BookList></BookList>
    </div>
</template>

<script>
    import BookList from "@/components/BookList";
    export default {
        name: "Library",
        components: {
            BookList
        }
    }
</script>

<style scoped>

</style>
