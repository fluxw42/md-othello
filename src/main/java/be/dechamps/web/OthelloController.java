package be.dechamps.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import be.dechamps.model.OthelloEngine;
import be.dechamps.model.Point;

/**
 * Servlet implementation class OthelloController
 */
/*public class OthelloController   {
	private static final String THINKING = "thinking";
	// private static final String POSITIONS = "positions";
	private static final String OTHELLO_ENGINE = "board";
	private static final long serialVersionUID = 1L;
	
	private List<String> logList = new ArrayList<String>();

*/	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
/*	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger("DOGET");
		String cmd = request.getParameter("cmd");
		
		
		
		HttpSession session = request.getSession();
		if (cmd == null)session.invalidate();
		session = request.getSession(true);
		logger("Controller:"+"Nieuw?"+session.isNew());
		OthelloEngine engine = getEngine(session);

		boolean thinking = isThinking(session);

		logger("CMD:" + cmd);
		if ("restart".equals(cmd)) {
			engine.restart();
			stopThinking(session);
		} else if ("computermove".equals(cmd) && !thinking) {
			engine.thinkAndMove();

		} else if ("evaluated".equals(cmd)) {
			// Set<OthelloPosition> pos = engine.getPositions();
			stopThinking(session);
		} else if (cmd != null && cmd.startsWith("level") && !thinking) {
			int level = Integer.valueOf(cmd.substring(6));
			engine.setDepth(level);
		}

		setRequestAttributes(request, true);
		RequestDispatcher dp = request
				.getRequestDispatcher("/WEB-INF/pages/othello.jsp");
		dp.forward(request, response);
	}

	private void stopThinking(HttpSession session) {
		session.setAttribute(THINKING, false);

	}

	private boolean isThinking(HttpSession session) {
		logger("ISTHINKING");
		boolean thinking = false;
		Boolean thinkingObj = (Boolean) session.getAttribute(THINKING);
		if (thinkingObj != null)
			thinking = thinkingObj;
		return thinking;
	}

	private void startThinking(HttpSession session) {
		session.setAttribute(THINKING, true);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger("DOPOST");
		String parameterString = request.getParameter("index");
		Integer parameter = parameterString == null ? 0 : Integer
				.valueOf(parameterString);
		String statusString = request.getParameter("status");
		Integer status = statusString == null ? 0 : Integer
				.valueOf(statusString);
		OthelloEngine engine = getEngine(request.getSession());
		HttpSession session = request.getSession();
		boolean validMove = false;
		boolean thinking = isThinking(session);
		logger(status+" "+parameterString);
		if (!thinking) {

			if (status == 1) {
				validMove = engine.makeMove(parameter);
				logger("validmove?"+parameter+" result:"+validMove+" engine nu:"+engine);
			} else if (status == 2) {
				startThinking(session);
				logger("Status2 engine voor computerzet:"+engine);
				engine.thinkAndMove();
				stopThinking(session);
				logger("Status2 engine na computerzet:"+engine);
			}
		}
		setRequestAttributes(request, validMove);
		RequestDispatcher dp = request
				.getRequestDispatcher("/WEB-INF/pages/paintboard.jsp");
		session.setAttribute(OTHELLO_ENGINE, engine);
		logger("net op sessie gezet:"+engine);
		dp.forward(request, response);
	}

	private void setRequestAttributes(HttpServletRequest request,
			boolean validMove) {
		OthelloEngine engine = getEngine(request.getSession());
		// request.setAttribute(POSITIONS, engine.getPositions());
		Point lastMove = engine.getLastMove();
		logger("Controller:lastmove "+lastMove);
		if (lastMove != null) {
			request.setAttribute("movex", lastMove.getX());
			request.setAttribute("movey", lastMove.getY());
		}
		request.setAttribute("validMove", validMove);
		request.setAttribute("depth", engine.getDepth());
		int white = engine.getWhite();
		request.setAttribute("white", white);
		int black = engine.getBlack();
		request.setAttribute("black", black);
		request.setAttribute("move", engine.isBlacksMove() ? "BLACK" : "WHITE");
		boolean gameOver = engine.isGameOver();
		String msg = "draw.";
		if (gameOver) {
			if (black > white)
				msg = "Black wins!";
			if (black < white)
				msg = "White wins!";
		}
		String gameOverMessage = " Game Over. " + msg;
		request.setAttribute("gameOver", gameOver ? gameOverMessage
				: " Playing");
		request.setAttribute("score", engine.getScore());
		request.setAttribute("log", logString());
	}

	private OthelloEngine getEngine(HttpSession session) {
		OthelloEngine engine = (OthelloEngine) session
				.getAttribute(OTHELLO_ENGINE);
		// engine.think();
		if (engine == null) {
			logger("Controller: nieuwe engine");
			// maak bord aan en zet het op de sessie
			engine = new OthelloEngine();
			session.setAttribute(OTHELLO_ENGINE, engine);
		}else{
			logger("Controller: Geen nieuwe engine."+engine);
			
		}
		return engine;
	}
	
	private void logger(String s){
		//logList.add(s);
	}
	private String logString(){
		int i=0;
		StringBuilder b= new StringBuilder();
		for(String s:logList){
			b.append((i++) +")"+s+"<br/>");
		}
		return b.toString();
	}
}
*/