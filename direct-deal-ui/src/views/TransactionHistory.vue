<template>
  <v-container fluid justify-center v-if="isLoaded" class="ma-0 pa-0">
    <v-card flat class="mx-auto">
      <v-tabs v-model="tabs" fixed-tabs>
        <v-tabs-slider></v-tabs-slider>
        <v-tab href="#tabs-selling" class="primary--text text-caption" @click="fetchSellerItems">Selling</v-tab>
        <v-tab href="#tabs-completed" class="primary--text text-caption" @click="fetchCompletedItems">Sale History</v-tab>
        <v-tab href="#tabs-buy-history" class="primary--text text-caption" @click="fetchBuyItems">Buy History</v-tab>
      </v-tabs>

      <!-- Selling List -->
      <v-tabs-items v-model="tabs">
        <v-tab-item value="tabs-selling">
          <v-card flat class="mx-auto">
            <v-list two-line v-for="item in items" :key="item.id" class="my-0 py-0">
              <v-card-text class="ma-0 pa-0">
                <v-list-item @click="selectItem(item.id)">
                  <v-list-item-avatar size="100" class="rounded" >
                    <v-img :src="'/api/v1/image/' + item.images[0]"></v-img>
                  </v-list-item-avatar>
                  <v-list-item-content class="mb-7">
                    <v-list-item-title>{{item.title}}</v-list-item-title>
                    <v-list-item-subtitle class="mt-1">
                      {{item.targetPrice}} USD
                    </v-list-item-subtitle>
                    <v-list-item-subtitle class="mt-1">
                      <v-chip x-small>
                        {{item.category}}
                      </v-chip>
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions class="ma-0 pa-0">
                <v-spacer></v-spacer>
                <v-btn text class="mx-3 primary--text text-caption font-weight-bold" @click="liftup(item)">Lifting Up</v-btn>
                <v-btn text class="mx-3 primary--text text-caption font-weight-bold" @click="complete(item)">Completed</v-btn>
              </v-card-actions>
              <v-divider></v-divider>
            </v-list>
          </v-card>
        </v-tab-item>

        <!-- Transaction History -->
        <v-tab-item value="tabs-completed">
          <v-card flat class="mx-auto">
            <v-list two-line v-for="item in completedItems" :key="item.id" class="my-0 py-0">
              <v-card-text class="ma-0 pa-0">
                <v-list-item @click="selectItem(item.itemId)">
                  <v-list-item-avatar size="100" class="rounded" >
                    <v-img :src="'/api/v1/image/' + item.mainImage"></v-img>
                  </v-list-item-avatar>
                  <v-list-item-content class="mb-7">
                    <v-list-item-title>{{item.title}}</v-list-item-title>
                    <v-list-item-subtitle class="mt-1">
                      {{item.targetPrice}} USD
                    </v-list-item-subtitle>
                    <v-list-item-subtitle class="mt-1">
                      <v-chip x-small>
                        {{item.category}}
                      </v-chip>
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions class="ma-0 pa-0 justify-center">
                <v-btn text class="primary--text text-caption font-weight-bold" @click="fetchCustomers(item)">Choosing the buyer</v-btn>
              </v-card-actions>
              <v-divider></v-divider>
            </v-list>
          </v-card>
        </v-tab-item>

        <!-- Buy History -->
        <v-tab-item value="tabs-buy-history">
          <v-card flat>
            <v-list two-line v-for="item in buyItems" :key="item.id" class="my-0 py-0">
                <v-list-item @click="selectItem(item.itemId)">
                  <v-list-item-avatar size="100" class="rounded" >
                    <v-img :src="'/api/v1/image/' + item.mainImage"></v-img>
                  </v-list-item-avatar>
                  <v-list-item-content class="mb-7">
                    <v-list-item-title>{{item.title}}</v-list-item-title>
                    <v-list-item-subtitle class="mt-1">
                      {{item.targetPrice}} USD
                    </v-list-item-subtitle>
                    <v-list-item-subtitle class="mt-1">
                      <v-chip x-small>
                        {{item.category}}
                      </v-chip>
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
                <v-divider></v-divider>
            </v-list>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>

    <!-- Dialog -->
    <v-dialog
      v-model="dialog"
      scrollable
      max-width="300px"
    >
      <v-card>
        <v-card-title>Buyer List</v-card-title>
        <v-card-text>
          <v-list v-for="(customer, index) in customers" :key="index" class="my-0 py-0">
            <v-list-item @click="setBuyer(customer)">
              <v-list-item-avatar class="green" >
                {{customer[0]}}
              </v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title>{{customer}}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
            <v-divider></v-divider>
          </v-list>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-container>

  <v-container fluid fill-height justify-center v-else>
    <div class="text-center">
      <v-progress-circular
        :size="70"
        color="primary"
        indeterminate
      ></v-progress-circular>
    </div>
  </v-container>
</template>

<script>
//import axios from "axios";
import api from "../axios"

export default {
  data: () => ({
    tabs: 'tabs-selling',
    isLoaded: true,
    items: [],
    completedItems: [],
    buyItems: [],
    dialog: false,
    customers: [],
    selectedCompletedItem: null
  }),
  methods: {
    /* selling list */
    async fetchSellerItems() {
      this.isLoaded = false;
      try {
        const response = await api({
          method: "GET",
          url: "/api/v1/saleitem/seller-items",
        });

        this.isLoaded = true;
        this.items = response.data;
      } catch (error) {
        console.log("Error", error);
      }
    },
    /* lift up */
    async liftup(item) {
      try {
        const response = await api({
          method: "PUT",
          url: "/api/v1/salelist/" + item.id + "/lift-up",
        });

        const liftUpResponse = response.data;
        if (liftUpResponse.result == "SUCCESS") {
            this.$router.push('/')
        } else if (liftUpResponse.result == "FAILURE") {
            const canLiftUpDate = new Date(item.createdDate)
            canLiftUpDate.setDate(canLiftUpDate.getDate() + liftUpResponse.intervalDays)
            alert("Lift-Up will be possible at " + canLiftUpDate.toLocaleString())
        } else {
            throw new Error("An incorrect response : " + liftUpResponse.result)
        }
      } catch (error) {
        console.log("Error", error.message);
      }
    },
    /* complete */
    async complete(item) {
      try {
        await api({
          method: "PUT",
          url: "/api/v1/item/" + item.id + "/complete",
        });

        this.tabs = 'tabs-completed'
        this.fetchCompletedItems();
      } catch (error) {
        console.log("Error", error);
      }
    },
    /* transaction-history */
    async fetchCompletedItems() {
      this.isLoaded = false;
      try {
        const response = await api({
          method: "GET",
          url: "/api/v1/transaction-history",
        });

        this.completedItems = response.data;
        this.isLoaded = true;
      } catch (error) {
        console.log("Error", error);
      }
    },
    /* buy-history */
    async fetchBuyItems() {
      this.isLoaded = false;
      try {
        const response = await api({
          method: "GET",
          url: "/api/v1/buy-history",
        });

        this.buyItems = response.data;
        this.isLoaded = true;
      } catch (error) {
        console.log("Error", error);
      }
    },
    /* customers */
    async fetchCustomers(item) {
      this.selectedCompletedItem = item;
      this.isLoaded = false;
      try {
        const response = await api({
          method: "GET",
          url: "/api/v1/chatting/" + item.itemId + "/customer-list",
        });

        this.customers = response.data;
        this.isLoaded = true;
        this.dialog = true;
      } catch (error) {
        console.log("Error", error);
      }
    },
    async setBuyer(customerId) {
      try {
        const response = await api({
          method: "PUT",
          url: "/api/v1/transaction-history/setbuyer",
          data: {
            id: this.selectedCompletedItem.id,
            buyerId: customerId
          }
        });

        this.customers = response.data;
      } catch (error) {
        console.log("Error", error);
      }

      this.dialog = false;
    },
    selectItem(itemId) {
      console.log("select item => " + itemId);
      //this.$router.params = {id: itemId}
      this.$store.commit("setRouterParams", {path: '/item-detail', value: { id: itemId }});
      this.$router.push("/item-detail");
      //this.$router.push({path: '/item-detail', params:{id: itemId}})
    },
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true);
    this.$store.commit("setSelectedBottomNavigationItem", "transaction");
    //axios.defaults.headers.common["Authorization"] = this.$store.state.authorization;
    this.fetchSellerItems();
  },
};
</script>
