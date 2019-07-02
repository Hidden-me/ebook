<template>
    <div>
        <div class="bg-primary text-white p-3">书籍管理</div>
        <BookList admin="true"></BookList>
    </div>
</template>

<script>
    import BookList from '@/components/BookList';
    export default {
        name: 'BookManager',
        components: {BookList}
    }
</script>

<style scoped>

</style>
