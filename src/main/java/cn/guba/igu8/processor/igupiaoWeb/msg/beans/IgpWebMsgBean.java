/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public class IgpWebMsgBean {

	private String id;

	private String kind;

	private String title;

	private String brief;

	private float price;

	private float vip_price;

	private String content;

	private String[] image_thumb;

	private String[] image;

	private int[] image_size;

	private String audio;

	private int audio_len;

	private long rec_time;

	private String rec_time_desc;

	private String url;

	private String state2;

	private boolean view_self;

	private boolean vip_show;

	private boolean not_vip_show;

	private String unshow_group;

	private String vip_group_info;

	private String o_id;

	private String o_kind;

	private String zh_id;

	private String b_id;

	private String g_id;

	private String menus_id;

	private String v_id;

	private String touid;

	private IgpStockBean[] stock_info;

	private String thunimg;

	private String o_c_id;

	private String label_kind;

	private String[] menus;

	private String[] forward_info;

	private boolean do_forward;

	private String[] to_info;

	private int comment_num;

	private int support_num;

	private int reward_num;

	private String[] reward_list;

	private int number;

	private String content_new;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind
	 *            the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the brief
	 */
	public String getBrief() {
		return brief;
	}

	/**
	 * @param brief
	 *            the brief to set
	 */
	public void setBrief(String brief) {
		this.brief = brief;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the vip_price
	 */
	public float getVip_price() {
		return vip_price;
	}

	/**
	 * @param vip_price
	 *            the vip_price to set
	 */
	public void setVip_price(float vip_price) {
		this.vip_price = vip_price;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the image_thumb
	 */
	public String[] getImage_thumb() {
		return image_thumb;
	}

	/**
	 * @param image_thumb
	 *            the image_thumb to set
	 */
	public void setImage_thumb(String[] image_thumb) {
		this.image_thumb = image_thumb;
	}

	/**
	 * @return the image
	 */
	public String[] getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String[] image) {
		this.image = image;
	}

	/**
	 * @return the image_size
	 */
	public int[] getImage_size() {
		return image_size;
	}

	/**
	 * @param image_size
	 *            the image_size to set
	 */
	public void setImage_size(int[] image_size) {
		this.image_size = image_size;
	}

	/**
	 * @return the audio
	 */
	public String getAudio() {
		return audio;
	}

	/**
	 * @param audio
	 *            the audio to set
	 */
	public void setAudio(String audio) {
		this.audio = audio;
	}

	/**
	 * @return the audio_len
	 */
	public int getAudio_len() {
		return audio_len;
	}

	/**
	 * @param audio_len
	 *            the audio_len to set
	 */
	public void setAudio_len(int audio_len) {
		this.audio_len = audio_len;
	}

	/**
	 * @return the rec_time
	 */
	public long getRec_time() {
		return rec_time;
	}

	/**
	 * @param rec_time
	 *            the rec_time to set
	 */
	public void setRec_time(long rec_time) {
		this.rec_time = rec_time;
	}

	/**
	 * @return the rec_time_desc
	 */
	public String getRec_time_desc() {
		return rec_time_desc;
	}

	/**
	 * @param rec_time_desc
	 *            the rec_time_desc to set
	 */
	public void setRec_time_desc(String rec_time_desc) {
		this.rec_time_desc = rec_time_desc;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the state2
	 */
	public String getState2() {
		return state2;
	}

	/**
	 * @param state2
	 *            the state2 to set
	 */
	public void setState2(String state2) {
		this.state2 = state2;
	}

	/**
	 * @return the vip_group_info
	 */
	public String getVip_group_info() {
		return vip_group_info;
	}

	/**
	 * @param vip_group_info
	 *            the vip_group_info to set
	 */
	public void setVip_group_info(String vip_group_info) {
		this.vip_group_info = vip_group_info;
	}

	/**
	 * @return the o_id
	 */
	public String getO_id() {
		return o_id;
	}

	/**
	 * @param o_id
	 *            the o_id to set
	 */
	public void setO_id(String o_id) {
		this.o_id = o_id;
	}

	/**
	 * @return the o_kind
	 */
	public String getO_kind() {
		return o_kind;
	}

	/**
	 * @param o_kind
	 *            the o_kind to set
	 */
	public void setO_kind(String o_kind) {
		this.o_kind = o_kind;
	}

	/**
	 * @return the zh_id
	 */
	public String getZh_id() {
		return zh_id;
	}

	/**
	 * @param zh_id
	 *            the zh_id to set
	 */
	public void setZh_id(String zh_id) {
		this.zh_id = zh_id;
	}

	/**
	 * @return the b_id
	 */
	public String getB_id() {
		return b_id;
	}

	/**
	 * @param b_id
	 *            the b_id to set
	 */
	public void setB_id(String b_id) {
		this.b_id = b_id;
	}

	/**
	 * @return the g_id
	 */
	public String getG_id() {
		return g_id;
	}

	/**
	 * @param g_id
	 *            the g_id to set
	 */
	public void setG_id(String g_id) {
		this.g_id = g_id;
	}

	/**
	 * @return the menus_id
	 */
	public String getMenus_id() {
		return menus_id;
	}

	/**
	 * @param menus_id
	 *            the menus_id to set
	 */
	public void setMenus_id(String menus_id) {
		this.menus_id = menus_id;
	}

	/**
	 * @return the v_id
	 */
	public String getV_id() {
		return v_id;
	}

	/**
	 * @param v_id
	 *            the v_id to set
	 */
	public void setV_id(String v_id) {
		this.v_id = v_id;
	}

	/**
	 * @return the touid
	 */
	public String getTouid() {
		return touid;
	}

	/**
	 * @param touid
	 *            the touid to set
	 */
	public void setTouid(String touid) {
		this.touid = touid;
	}

	/**
	 * @return the stock_info
	 */
	public IgpStockBean[] getStock_info() {
		return stock_info;
	}

	/**
	 * @param stock_info
	 *            the stock_info to set
	 */
	public void setStock_info(IgpStockBean[] stock_info) {
		this.stock_info = stock_info;
	}

	/**
	 * @return the thunimg
	 */
	public String getThunimg() {
		return thunimg;
	}

	/**
	 * @param thunimg
	 *            the thunimg to set
	 */
	public void setThunimg(String thunimg) {
		this.thunimg = thunimg;
	}

	/**
	 * @return the o_c_id
	 */
	public String getO_c_id() {
		return o_c_id;
	}

	/**
	 * @param o_c_id
	 *            the o_c_id to set
	 */
	public void setO_c_id(String o_c_id) {
		this.o_c_id = o_c_id;
	}

	/**
	 * @return the label_kind
	 */
	public String getLabel_kind() {
		return label_kind;
	}

	/**
	 * @param label_kind
	 *            the label_kind to set
	 */
	public void setLabel_kind(String label_kind) {
		this.label_kind = label_kind;
	}

	/**
	 * @return the menus
	 */
	public String[] getMenus() {
		return menus;
	}

	/**
	 * @param menus
	 *            the menus to set
	 */
	public void setMenus(String[] menus) {
		this.menus = menus;
	}

	/**
	 * @return the forward_info
	 */
	public String[] getForward_info() {
		return forward_info;
	}

	/**
	 * @param forward_info
	 *            the forward_info to set
	 */
	public void setForward_info(String[] forward_info) {
		this.forward_info = forward_info;
	}

	/**
	 * @return the do_forward
	 */
	public boolean isDo_forward() {
		return do_forward;
	}

	/**
	 * @param do_forward
	 *            the do_forward to set
	 */
	public void setDo_forward(boolean do_forward) {
		this.do_forward = do_forward;
	}

	/**
	 * @return the to_info
	 */
	public String[] getTo_info() {
		return to_info;
	}

	/**
	 * @param to_info
	 *            the to_info to set
	 */
	public void setTo_info(String[] to_info) {
		this.to_info = to_info;
	}

	/**
	 * @return the comment_num
	 */
	public int getComment_num() {
		return comment_num;
	}

	/**
	 * @param comment_num
	 *            the comment_num to set
	 */
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}

	/**
	 * @return the support_num
	 */
	public int getSupport_num() {
		return support_num;
	}

	/**
	 * @param support_num
	 *            the support_num to set
	 */
	public void setSupport_num(int support_num) {
		this.support_num = support_num;
	}

	/**
	 * @return the reward_num
	 */
	public int getReward_num() {
		return reward_num;
	}

	/**
	 * @param reward_num
	 *            the reward_num to set
	 */
	public void setReward_num(int reward_num) {
		this.reward_num = reward_num;
	}

	/**
	 * @return the reward_list
	 */
	public String[] getReward_list() {
		return reward_list;
	}

	/**
	 * @param reward_list
	 *            the reward_list to set
	 */
	public void setReward_list(String[] reward_list) {
		this.reward_list = reward_list;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the content_new
	 */
	public String getContent_new() {
		return content_new;
	}

	/**
	 * @param content_new
	 *            the content_new to set
	 */
	public void setContent_new(String content_new) {
		this.content_new = content_new;
	}

	/**
	 * @return the view_self
	 */
	public boolean isView_self() {
		return view_self;
	}

	/**
	 * @param view_self
	 *            the view_self to set
	 */
	public void setView_self(boolean view_self) {
		this.view_self = view_self;
	}

	/**
	 * @return the vip_show
	 */
	public boolean isVip_show() {
		return vip_show;
	}

	/**
	 * @param vip_show
	 *            the vip_show to set
	 */
	public void setVip_show(boolean vip_show) {
		this.vip_show = vip_show;
	}

	/**
	 * @return the not_vip_show
	 */
	public boolean isNot_vip_show() {
		return not_vip_show;
	}

	/**
	 * @param not_vip_show
	 *            the not_vip_show to set
	 */
	public void setNot_vip_show(boolean not_vip_show) {
		this.not_vip_show = not_vip_show;
	}

	/**
	 * @return the unshow_group
	 */
	public String getUnshow_group() {
		return unshow_group;
	}

	/**
	 * @param unshow_group
	 *            the unshow_group to set
	 */
	public void setUnshow_group(String unshow_group) {
		this.unshow_group = unshow_group;
	}

}
