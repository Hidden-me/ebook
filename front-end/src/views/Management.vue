<template>
    <div class="row">
        <!-- Management Entries -->
        <div class="col-2">
            <button v-for="(entry, index) in entries" v-bind:key="index"
                    class="btn btn-block p-4" v-on:click="switchEntry(index)">{{ entry }}</button>
        </div>
        <!-- Management Component -->
        <div class="col-10">
            <BookManager v-if="selectedEntry==0"></BookManager>
            <OrderManager v-else-if="selectedEntry==1"></OrderManager>
            <UserManager v-else-if="selectedEntry==2"></UserManager>
            <span v-else class="text-center">请从左侧选择一项功能 :)</span>
        </div>
    </div>
</template>

<script>
    import BookManager from '@/components/BookManager';
    import OrderManager from '@/components/OrderManager';
    import UserManager from '@/components/UserManager';
    export default {
        name: 'Management',
        components: {UserManager, OrderManager, BookManager},
        methods:{
            switchEntry: function (index) {
                if(index >= 0 && index < this.entries.length){
                    this.selectedEntry = index;
                }
            }
        },
        data: function () {
            return {
                entries: ['书籍管理', '订单管理', '用户管理'],
                selectedEntry: -1
            };
        }
    }
</script>

<style scoped>

</style>
