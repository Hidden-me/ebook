<template>
    <div class="row align-items-center">
        <div class="col-2">
            <img src="@/assets/logo.png" width="150px" height="160px" v-bind:alt="title"/>
        </div>
        <div class="col-6">
            <div class="text-center">
                <div>{{ author }} - {{ title }}</div>
                <div>ISBN：{{ item.book.isbn }}</div>
                <div>单价：￥{{ item.price }}</div>
                <div>购买数量：{{ item.count }}</div>
            </div>
        </div>
        <div class="col-2">
            <button class="btn btn-outline-primary w-100 mb-2" v-on:click="openCounter()">修改数量</button>
            <button class="btn btn-outline-danger w-100" v-on:click="removeFromCart()">移出购物车</button>
        </div>
        <div class="col-2 text-primary h4">
            ￥{{ itemPrice }}
        </div>
        <!-- Counter & mask -->
        <div v-if="counterOn">
            <div class="mask"></div>
            <div class="on-mask bg-white window-sm p-4">
                <button class="btn btn-outline-danger col-3 offset-9 mb-3"
                        v-on:click="closeCounter()">取消</button>
                <div class="text-center mt-2">
                    <span>修改数量</span>
                </div>
                <div class="input-group col-8 offset-2 mt-2">
                    <div class="input-group-prepend">
                        <button class="btn btn-outline-secondary" v-bind:disabled="isMinCount"
                                v-on:click="decreaseCount()">-</button>
                    </div>
                    <input type="text" class="form-control text-center" v-model.number="count"
                           placeholder="数量"/>
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" v-bind:disabled="isMaxCount"
                                v-on:click="increaseCount()">+</button>
                    </div>
                </div>
                <div class="row mt-3">
                    <button class="btn btn-outline-secondary col-3 offset-2"
                            v-on:click="closeCounter()">取消</button>
                    <button class="btn btn-outline-success col-3 offset-2"
                            v-on:click="commitCount()">完成</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'CartItem',
        props: ['item'],
        computed: {
            itemPrice: function () {
                return this.item.price * this.item.count;
            },
            author: function () {
                if(this.item.book.author.length === 0){
                    return '佚名';
                }
                return this.item.book.author;
            },
            title: function () {
                if(this.item.book.title.length === 0){
                    return '无题';
                }
                return this.item.book.title;
            },
            isMaxCount: function () {
                return this.count >= this.item.book.stock;
            },
            isMinCount: function () {
                return this.count <= 1;
            }
        },
        methods: {
            removeFromCart: function () {
                this.$http.post(
                    'cart/remove', {
                        isbn: this.item.book.isbn
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.$emit('cart-update');
                        }
                    }, ()=>{}
                );
            },
            openCounter: function () {
                this.count = parseInt(this.item.count);
                this.counterOn = true;
            },
            closeCounter: function () {
                this.counterOn = false;
            },
            increaseCount: function () {
                if(!this.isMaxCount){
                    this.count++;
                }
            },
            decreaseCount: function () {
                if(!this.isMinCount){
                    this.count--;
                }
            },
            commitCount: function () {
                this.$http.post(
                    'cart/set', {
                        isbn: this.item.book.isbn,
                        count: this.count
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.$emit('cart-update');
                        }
                        this.closeCounter();
                    }, ()=>{
                        alert('服务器无响应');
                    }
                );
            }
        },
        data: function () {
            return {
                counterOn: false,
                count: 0
            };
        }
    }
</script>

<style scoped>

</style>
