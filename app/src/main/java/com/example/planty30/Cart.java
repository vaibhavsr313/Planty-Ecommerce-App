package com.example.planty30;

public class Cart {

        private String pName,pQuan,pPrice;

        public Cart() {

        }

        public Cart(String pName, String pQuan, String pPrice) {
                this.pName = pName;
                this.pQuan = pQuan;
                this.pPrice = pPrice;
        }

        public String getpName() {
                return pName;
        }

        public void setpName(String pName) {
                this.pName = pName;
        }

        public String getpQuan() {
                return pQuan;
        }

        public void setpQuan(String pQuan) {
                this.pQuan = pQuan;
        }

        public String getpPrice() {
                return pPrice;
        }

        public void setpPrice(String pPrice) {
                this.pPrice = pPrice;
        }
}
