package main;

import model.Pedido;

public interface OnMessageListener {

	void onMessage(Pedido pedido, String ip);
}
