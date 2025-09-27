<template>
  <v-container fill-height justify-center>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-row justify-center>
        <v-col cols="12" sm="10" md="8" lg="6">
          <v-card min-width="350" max-width="450">
            <v-card-text>
              <v-text-field
                v-model="name"
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
//import axios from "axios";
import api from "../axios"

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
    async submit() {
      this.validate();

      const data = {
        name: this.name,
        email: this.email,
        password: this.password,
      };

      const header = {
        "Content-Type": "application/json",
      };

      try {
        await api.post("/api/v1/account", data, header)
        this.$router.push("/login")
      } catch(error) {
        console.log("Error", error.response.data.message);
        alert(error.response.data.message);
      }
    },
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", false);
  },
};
</script>
