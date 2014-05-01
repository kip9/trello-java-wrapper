package com.julienvey.trello.impl;

import static com.julienvey.trello.impl.TrelloUrl.ADD_LABEL_TO_CARD;
import static com.julienvey.trello.impl.TrelloUrl.CREATE_CARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_BOARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_CARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_ENTITIES;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_LIST;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_MEMBER;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_MEMBER_CREATOR;
import static com.julienvey.trello.impl.TrelloUrl.GET_ACTION_ORGANIZATION;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_ACTIONS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_CARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_CARDS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_CHECKLISTS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_LISTS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_MEMBERS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_MEMBERS_INVITED;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_MEMBER_CARDS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_MYPREFS;
import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_ORGANIZATION;
import static com.julienvey.trello.impl.TrelloUrl.GET_CARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_CARD_ACTIONS;
import static com.julienvey.trello.impl.TrelloUrl.GET_CARD_ATTACHMENT;
import static com.julienvey.trello.impl.TrelloUrl.GET_CARD_ATTACHMENTS;
import static com.julienvey.trello.impl.TrelloUrl.GET_CARD_BOARD;
import static com.julienvey.trello.impl.TrelloUrl.GET_LIST;
import static com.julienvey.trello.impl.TrelloUrl.GET_MEMBER;
import static com.julienvey.trello.impl.TrelloUrl.createUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.TrelloHttpClient;
import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Attachment;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.CardWithActions;
import com.julienvey.trello.domain.CheckList;
import com.julienvey.trello.domain.Entity;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.MyPrefs;
import com.julienvey.trello.domain.Organization;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.domaininternal.Label;
import com.julienvey.trello.impl.http.RestTemplateHttpClient;

public class TrelloImpl implements Trello {

  private TrelloHttpClient httpClient;
  private String applicationKey;
  private String accessToken;

  private static Logger logger = LoggerFactory.getLogger(TrelloImpl.class);

  // FIXME : remove me
  public TrelloImpl(String applicationKey, String accessToken) {
    this(applicationKey, accessToken, new RestTemplateHttpClient());
  }

  public TrelloImpl(String applicationKey, String accessToken, TrelloHttpClient httpClient) {
    this.applicationKey = applicationKey;
    this.accessToken = accessToken;
    this.httpClient = httpClient;
  }

  /* Boards */

  @Override
  public Board getBoard(String boardId, Argument... args) {
    Board board = get(createUrl(GET_BOARD).params(args).asString(), Board.class, boardId);
    board.setInternalTrello(this);
    return board;
  }

  @Override
  public List<Action> getBoardActions(String boardId, Argument... args) {
    List<Action> actions = Arrays.asList(get(createUrl(GET_BOARD_ACTIONS).params(args).asString(), Action[].class,
        boardId));
    for (Action action : actions) {
      action.setInternalTrello(this);
    }
    return actions;
  }

  @Override
  public List<Card> getBoardCards(String boardId, Argument... args) {
    List<Card> cards = Arrays.asList(get(createUrl(GET_BOARD_CARDS).params(args).asString(), Card[].class, boardId));
    for (Card card : cards) {
      card.setInternalTrello(this);
    }
    return cards;
  }

  @Override
  public Card getBoardCard(String boardId, String cardId, Argument... args) {
    Card card = get(createUrl(GET_BOARD_CARD).params(args).asString(), Card.class, boardId, cardId);
    card.setInternalTrello(this);
    return card;
  }

  @Override
  public List<CheckList> getBoardChecklists(String boardId, Argument... args) {
    List<CheckList> checkLists = Arrays.asList(get(createUrl(GET_BOARD_CHECKLISTS).params(args).asString(),
        CheckList[].class, boardId));
    for (CheckList checkList : checkLists) {
      checkList.setInternalTrello(this);
    }
    return checkLists;
  }

  @Override
  public List<TList> getBoardLists(String boardId, Argument... args) {
    List<TList> tLists = Arrays.asList(get(createUrl(GET_BOARD_LISTS).params(args).asString(), TList[].class, boardId));
    for (TList list : tLists) {
      list.setInternalTrello(this);

      for (Card card : list.getCards()) {
        card.setInternalTrello(this);
      }
    }
    return tLists;
  }

  @Override
  public List<Member> getBoardMembers(String boardId, Argument... args) {
    List<Member> members = Arrays.asList(get(createUrl(GET_BOARD_MEMBERS).params(args).asString(), Member[].class,
        boardId));
    for (Member member : members) {
      member.setInternalTrello(this);
    }
    return members;
  }

  @Override
  public List<Card> getBoardMemberCards(String boardId, String memberId, Argument... args) {
    List<Card> cards = Arrays.asList(get(createUrl(GET_BOARD_MEMBER_CARDS).params(args).asString(), Card[].class,
        boardId, memberId));
    for (Card card : cards) {
      card.setInternalTrello(this);
    }
    return cards;
  }

  //FIXME Remove this method
  @Override
  @Deprecated
  public List<CardWithActions> getBoardMemberActivity(String boardId, String memberId, String actionFilter,
      Argument... args) {
    if (actionFilter == null)
      actionFilter = "all";
    Argument[] argsAndFilter = Arrays.copyOf(args, args.length + 1);
    argsAndFilter[args.length] = new Argument("actions", actionFilter);

    List<CardWithActions> cards = Arrays.asList(get(createUrl(GET_BOARD_MEMBER_CARDS).params(argsAndFilter).asString(),
        CardWithActions[].class, boardId, memberId));
    for (Card card : cards) {
      card.setInternalTrello(this);
    }
    return cards;
  }

  @Override
  public List<Member> getBoardMembersInvited(String boardId, Argument... args) {
    List<Member> members = Arrays.asList(get(createUrl(GET_BOARD_MEMBERS_INVITED).params(args).asString(),
        Member[].class, boardId));
    for (Member member : members) {
      member.setInternalTrello(this);
    }
    return members;
  }

  @Override
  public MyPrefs getBoardMyPrefs(String boardId) {
    MyPrefs myPrefs = get(createUrl(GET_BOARD_MYPREFS).asString(), MyPrefs.class, boardId);
    myPrefs.setInternalTrello(this);
    return myPrefs;
  }

  @Override
  public Organization getBoardOrganization(String boardId, Argument... args) {
    Organization organization = get(createUrl(GET_BOARD_ORGANIZATION).params(args).asString(), Organization.class,
        boardId);
    organization.setInternalTrello(this);
    return organization;
  }

  /* Action */

  @Override
  public Action getAction(String actionId, Argument... args) {
    Action action = get(createUrl(GET_ACTION).params(args).asString(), Action.class, actionId);
    action.setInternalTrello(this);
    return action;
  }

  @Override
  public Board getActionBoard(String actionId, Argument... args) {
    Board board = get(createUrl(GET_ACTION_BOARD).params(args).asString(), Board.class, actionId);
    board.setInternalTrello(this);
    return board;
  }

  @Override
  public Card getActionCard(String actionId, Argument... args) {
    Card card = get(createUrl(GET_ACTION_CARD).params(args).asString(), Card.class, actionId);
    card.setInternalTrello(this);
    return card;
  }

  @Override
  public List<Entity> getActionEntities(String actionId) {
    List<Entity> entities = Arrays.asList(get(createUrl(GET_ACTION_ENTITIES).asString(), Entity[].class, actionId));
    for (Entity entity : entities) {
      entity.setInternalTrello(this);
    }
    return entities;
  }

  @Override
  public TList getActionList(String actionId, Argument... args) {
    TList tList = get(createUrl(GET_ACTION_LIST).params(args).asString(), TList.class, actionId);
    tList.setInternalTrello(this);
    return tList;
  }

  @Override
  public Member getActionMember(String actionId, Argument... args) {
    Member member = get(createUrl(GET_ACTION_MEMBER).params(args).asString(), Member.class, actionId);
    member.setInternalTrello(this);
    return member;
  }

  @Override
  public Member getActionMemberCreator(String actionId, Argument... args) {
    Member member = get(createUrl(GET_ACTION_MEMBER_CREATOR).params(args).asString(), Member.class, actionId);
    member.setInternalTrello(this);
    return member;
  }

  @Override
  public Organization getActionOrganization(String actionId, Argument... args) {
    Organization organization = get(createUrl(GET_ACTION_ORGANIZATION).params(args).asString(), Organization.class,
        actionId);
    organization.setInternalTrello(this);
    return organization;
  }

  @Override
  public Card getCard(String cardId, Argument... args) {
    Card card = get(createUrl(GET_CARD).params(args).asString(), Card.class, cardId);
    card.setInternalTrello(this);
    return card;
  }

  @Override
  public List<Action> getCardActions(String cardId, Argument... args) {
    List<Action> actions = Arrays.asList(get(createUrl(GET_CARD_ACTIONS).params(args).asString(), Action[].class,
        cardId));
    for (Action action : actions) {
      action.setInternalTrello(this);
    }
    return actions;
  }

  @Override
  public List<Attachment> getCardAttachments(String cardId, Argument... args) {
    List<Attachment> attachments = Arrays.asList(get(createUrl(GET_CARD_ATTACHMENTS).params(args).asString(),
        Attachment[].class, cardId));
    for (Attachment attachment : attachments) {
      attachment.setInternalTrello(this);
    }
    return attachments;

  }

  @Override
  public Attachment getCardAttachment(String cardId, String attachmentId, Argument... args) {
    Attachment attachment = get(createUrl(GET_CARD_ATTACHMENT).params(args).asString(), Attachment.class, cardId,
        attachmentId);
    attachment.setInternalTrello(this);
    return attachment;
  }

  @Override
  public Board getCardBoard(String cardId, Argument... args) {
    Board board = get(createUrl(GET_CARD_BOARD).params(args).asString(), Board.class, cardId);
    board.setInternalTrello(this);
    return board;
  }

  /* Lists */

  @Override
  public TList getList(String listId, Argument... args) {
    TList tList = get(createUrl(GET_LIST).params(args).asString(), TList.class, listId);
    tList.setInternalTrello(this);
    return tList;
  }

  /* Others */

  @Override
  public Card createCard(String listId, Card card) {
    card.setIdList(listId);
    Card createdCard = postForObject(createUrl(CREATE_CARD).asString(), card, Card.class);
    createdCard.setInternalTrello(this);
    return createdCard;
  }

  @Override
  //FIXME Remove this method
  @Deprecated
  public Member getBasicMemberInformation(String username) {
    Member member = get(createUrl(GET_MEMBER).params(new Argument("fields", "username,fullName")).asString(),
        Member.class, username);
    member.setInternalTrello(this);
    return member;
  }

  @Override
  public Member getMemberInformation(String username) {
    Member member = get(createUrl(GET_MEMBER).asString(), Member.class, username);
    member.setInternalTrello(this);
    return member;
  }

  public void addLabelsToCard(String idCard, String[] labels) {
    for (String label : labels) {
      postForLocation(createUrl(ADD_LABEL_TO_CARD).asString(), new Label(label), idCard);
    }
  }

  /* internal methods */

  private <T> T postForObject(String url, T object, Class<T> objectClass, String... params) {
    logger.debug("PostForObject request on Trello API at url {} for class {} with params {}", url,
        objectClass.getCanonicalName(), params);
    return httpClient.postForObject(url, object, objectClass, enrichParams(params));
  }

  private void postForLocation(String url, Object object, String... params) {
    logger.debug("PostForLocation request on Trello API at url {} for class {} with params {}", url, object.getClass()
        .getCanonicalName(), params);
    httpClient.postForLocation(url, object, enrichParams(params));
  }

  private <T> T get(String url, Class<T> objectClass, String... params) {
    logger.debug("Get request on Trello API at url {} for class {} with params {}", url,
        objectClass.getCanonicalName(), params);
    return httpClient.get(url, objectClass, enrichParams(params));
  }

  private String[] enrichParams(String[] params) {
    List<String> paramList = new ArrayList<String>(Arrays.asList(params));
    paramList.add(applicationKey);
    paramList.add(accessToken);
    return paramList.toArray(new String[paramList.size()]);
  }
}
