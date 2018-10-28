package web.com.pizzaorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by gangi on 7/1/2018.
 */

// Home Activity to show Two Order Buttons.
public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    // On Click of Order Coffe
    public void orderCoffe(View view) {
        Intent intent = new Intent(HomeActivity.this, CoffeOrder.class);
        startActivity(intent);
    }

    // On Click of Order Pizza
    public void orderPizza(View view) {
        Intent intent = new Intent(HomeActivity.this, PizzaOrder.class);
        startActivity(intent);
    }

}
