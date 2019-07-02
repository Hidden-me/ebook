<template>
    <div>
        <div v-if="gettingCart">购物车加载中……</div>
        <div v-else-if="getCartFail">购物车加载失败……</div>
        <div v-else>
            <div v-for="(item, index) in cart.items" v-bind:key="index">
                <CartItem v-bind:item="item" v-on:cart-update="updateCartContent()"></CartItem>
            </div>
            <hr/>
            <!-- Total price & 'pay' button -->
            <div class="col-2 offset-10 text-primary h3 pt-3 pb-3">
                总计：￥{{ totalPrice }}
            </div>
            <hr/>
            <button class="btn btn-block btn-outline-success col-6 offset-3"
                    v-bind:disabled="emptyCart" v-on:click="submitOrder()">买单！</button>
        </div>
    </div>
</template>

<script>
    import CartItem from "@/components/CartItem";
    export default {
        name: 'CartContent',
        components: {CartItem},
        created: function () {
            this.requestCartContent();
        },
        computed: {
            totalPrice: function () {
                var i = 0, sum = 0;
                for(i = 0; i < this.cart.items.length; i++){
                    sum += this.cart.items[i].price * this.cart.items[i].count;
                }
                return sum;
            },
            emptyCart: function () {
                return this.cart.items.length <= 0;
            }
        },
        methods: {
            requestCartContent: function () {
                this.gettingCart = true;
                this.updateCartContent();
            },
            updateCartContent: function () {
                this.$http.post(
                    'cart'
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'success'){
                            this.cart = resp.data.cart;
                            this.getCartFail = false;
                        }else{
                            this.getCartFail = true;
                        }
                        this.gettingCart = false;
                    }, ()=>{
                        this.getCartFail = true;
                        this.gettingCart = false;
                    }
                );
            },
            submitOrder: function () {
                this.$http.post(
                    'cart/submit'
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'success'){
                            alert('订单提交成功！将跳转到订单界面……');
                            this.$router.push({name: 'order'});
                        }else{
                            alert('订单提交失败……');
                        }
                    }, ()=>{
                        alert('订单提交无响应……');
                    }
                );
            }
        },
        data: function () {
            return {
                cart: {
                    items: []
                },
                getCartFail: false,
                gettingCart: false
            };
        }
    }
</script>

<style scoped>

</style>
