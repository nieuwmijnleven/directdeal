<template>
  <v-container fill-height justify-center>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-row justify-center>
        <v-col cols="12" sm="10" md="8" lg="6">
          <v-card min-width="350" max-width="450">
            <v-card-text>
              <v-text-field
                v-model="name"
                :counter="30"
                :rules="nameRules"
                label="Name"
                required
              ></v-text-field>

              <v-text-field
                v-model="email"
                :rules="emailRules"
                label="E-mail"
                required
              ></v-text-field>

              <v-text-field
                v-model="password"
                :counter="6"
                :rules="passwordRules"
                label="Password"
                type="password"
                required
              ></v-text-field>
            </v-card-text>

            <v-divider class="mt-2"></v-divider>

            <v-card-actions>
              <v-btn text @click="cancel"> Cancel </v-btn>
              <v-spacer></v-spacer>
              <v-btn color="primary" text @click="submit"> Submit </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data: () => ({
    valid: true,
    name: "",
    nameRules: [
      (v) => !!v || "This field is required",
      (v) => (v && v.length <= 30) || "Name must be less than 30 characters",
    ],
    password: "",
    passwordRules: [
      (v) => !!v || "Password is required",
      (v) => (v && v.length >= 6) || "Password must be more than 6 characters",
    ],
    email: "",
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ],
  }),

  methods: {
    cancel() {
      this.name = "";
      this.email = "";
      this.password = "";
    },
    validate() {
      this.$refs.form.validate();
    },
    submit() {
      this.validate();

      const data = {
        name: this.name,
        email: this.email,
        password: this.password,
      };

      const header = {
        "Content-Type": "application/json",
      };

      axios
        .post("http://localhost:8084/api/v1/account", data, header)
        .then((response) => {
          // console.log(response)
          if (response.status == 201) {
            this.$router.push("/login");
          } else {
            alert("An incorrect response code is returned");
          }
        })
        .catch((error) => {
          if (error.response) {
            alert(error.response.data.error);
            console.log(error.response.data.message);
            // console.log(error.response.status)
            // console.log(error.response.headers)
          } else if (error.request) {
            console.log(error.request);
          } else {
            console.log("Error", error.message);
          }
        });
    },
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", false);
  },
};
</script>
