<template>
    <div>
        <div class="bg-info text-white p-4 mb-3">
            <span>这些是您放入购物车的图书！</span>
            <br/><span class="font-weight-bold">支付前请务必确认图书信息无误！</span>
            <br/><span class="font-italic">购物车中的所有图书会在服务器关闭时清空。</span>
        </div>
        <CartContent></CartContent>
    </div>
</template>

<script>
    import CartContent from '@/components/CartContent';
    export default {
        name: 'Cart',
        components: {CartContent}
    }
</script>

<style scoped>

</style>
