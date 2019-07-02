<template>
    <div>
        <OrderList admin="true"></OrderList>
    </div>
</template>

<script>
    import OrderList from '@/components/OrderList.vue';
    export default {
        name: 'OrderManager',
        components: {OrderList}
    }
</script>

<style scoped>

</style>
