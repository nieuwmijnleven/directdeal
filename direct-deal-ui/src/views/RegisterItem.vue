<template>
  <v-container fill-height justify-center>
    <v-row min-width="350" max-width="700" justify-center>
      <v-col cols="12">
        <v-card>
          <v-card-text>
            <v-form ref="form" v-model="valid" lazy-validation>
              <v-file-input
                v-model="files"
                :counter="10"
                :rules="filesRules"
                accept="image/*"
                label="Images"
                chips
                multiple
                required
              ></v-file-input>

              <v-text-field
                v-model="title"
                :counter="128"
                :rules="titleRules"
                label="Title"
                required
              ></v-text-field>

              <v-select
                v-model="selectedCategory"
                :items="category"
                :rules="categoryRules"
                label="Category"
                required
              ></v-select>

              <v-row align="center" class="mx-0">
                <v-col cols="7" class="mx-0 px-0">
                  <v-text-field
                    v-model="price"
                    :rules="priceRules"
                    label="Price"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="4" offset="1" class="mx-0">
                  <v-checkbox
                    v-model="discountable"
                    label="Discountable"
                  ></v-checkbox>
                </v-col>
              </v-row>

              <v-textarea
                v-model="text"
                :counter="1024"
                :rules="textRules"
                label="Introduce your item"
                placeholder="Please, introduce your item to sell."
                required
              ></v-textarea>
            </v-form>
          </v-card-text>

          <v-divider class="mt-2"></v-divider>

          <v-card-actions>
            <v-btn text @click="reset"> Cancel </v-btn>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="submit"> Submit </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="dialog" max-width="200px">
      <v-list>
        <v-list-item>
          <v-list-item-avatar>
              <v-progress-circular
                :size="30"
                color="primary"
                indeterminate
              ></v-progress-circular>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>processing...</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-dialog>

  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data: () => ({
    valid: true,
    discountable: false,
    uploadedImages: [],
    files: [],
    filesRules: [
      (v) => (v && v.length > 0) || "Images are required",
      (v) =>
        (v && v.length <= 10) || "The number of images must be less than 10",
    ],
    title: "",
    titleRules: [
      (v) => !!v || "Title is required",
      (v) => (v && v.length <= 128) || "Title must be less than 128 characters",
    ],
    selectedCategory: null,
    category: [
      "Computers",
      "Electronics",
      "Home & Kitchen",
      "Foods",
      "Beauty & Personal Care",
      "Books",
      "Women's Fashion",
      "Men's Fashion",
      "Etc."
    ],
    categoryRules: [
      (v) => !!v || "Category is required"
    ],
    price: "",
    priceRules: [
      (v) => !!v || "Price is required",
      (v) => (v && parseInt(v) > 0) || "Price must be a positive number",
    ],
    text: "",
    textRules: [
      (v) => !!v || "Text is required",
      (v) =>
        (v && v.length <= 1024) || "Text must be less than 1024 characters",
    ],
    dialog: false
  }),
  methods: {
    validate() {
      return this.$refs.form.validate();
    },
    reset() {
      this.$refs.form.reset();
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
    async submit() {
      if (!this.validate() || !this.files) return;

      this.dialog = true

      let formData = new FormData();
      for (let file of this.files) {
        formData.append("files", file, file.name);
      }

      try {
        let response = await axios.post(
          "http://localhost:8084/api/v1/image",
          formData
        );
        if (response.status == 201) {
          this.uploadedImages = response.data.images;
          console.log(this.uploadedImages);

          response = await axios({
            method: "POST",
            url: "http://localhost:8084/api/v1/item",
            data: {
              title: this.title,
              category: this.selectedCategory,
              targetPrice: this.price,
              discountable: this.discountable,
              text: this.text,
              images: this.uploadedImages,
            },
            headers: {
              "Content-Type": "application/json",
            },
          });

          if (response.status == 201) {
            const newItemId = response.data.itemId;

            for (let i = 0; i < 10; ++i) {
              try {
                response = await axios({
                  method: "GET",
                  url: "http://localhost:8084/api/v1/saleitem/" + newItemId,
                });

                if (response.status == 200) {
                  this.$router.push("/");
                  break;
                }
              } catch (error) {
                console.log(error);
              }

              this.sleep(1000);
            }
          } else {
            alert("An incorrect response code is returned: " + response.status);
          }
        } else {
          alert("An incorrect response code is returned: " + response.status);
        }
      } catch (error) {
        if (error.response) {
          alert(error.response.data.error);
          console.log(error.response.data.message);
        } else if (error.request) {
          console.log(error.request);
        } else {
          console.log("Error", error.message);
        }
      }

      this.dialog = false
    },
    sleep(ms) {
      const wakeUpTime = Date.now() + ms;
      while (Date.now() < wakeUpTime);
    },
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true);
    this.$store.commit("setSelectedBottomNavigationItem", "register-item");
    axios.defaults.headers.common["Authorization"] =
      this.$store.state.authorization;
  },
};
</script>
