package com.example.evangarcia.lab2;

/**
 * Created by evang_000 on 11/3/2016.
 */

public class Album {

        //Get and set Album Name
        private int mNameResId;

        public int getmNameResId() {
            return mNameResId;
        }

        public void setmNameResId(int NameResId) {
            this.mNameResId = NameResId;
        }

        //Get and set Release Year
        private int mYear;

        public int getmYear() {return mYear;}

        public void setmYear(int NameResId) {
            this.mYear = mYear;
        }

        //Get and set image id
        int mImageResId;

        public int getmImageResId() { return mImageResId; }

        public void setmImageResId(int ImageResId) {
            this.mImageResId = ImageResId;
        }

        //Get and set rating id
        float mRatingId;

        public float getmRatingId() {return mRatingId;}

        public void setmRatingId(float RatingResId) {this.mRatingId = RatingResId;}

        //Get and set content descriptor
        private int mContentDescriptor;

        public int getmContentDescriptor() {return mContentDescriptor;}

        public void setmContentDescriptor(int imageViewContentDescripor) {this.mContentDescriptor = imageViewContentDescripor;}


        //Constructor for Arctic Monkeys Albums
        public Album(int nameResId, int imageResId, int year, float rating, int content) {

            mNameResId = nameResId;
            mImageResId = imageResId;
            mYear = year;
            mRatingId = rating;
            mContentDescriptor = content;
        }
    }

